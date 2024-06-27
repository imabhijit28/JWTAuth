package com.jwt.project.JWT.Security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private Logger logger=LoggerFactory.getLogger(OncePerRequestFilter.class);
	
	@Autowired
	private JwtHelper jwtHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String requestHeader=request.getHeader("Authorization");
		logger.info("Header : {}",requestHeader);
		String userName = null;
		String token = null;
		
		if(requestHeader != null && requestHeader.startsWith("Bearer ")) {
			token = requestHeader.substring(7);
			try {
				userName = this.jwtHelper.getUsernameFromToken(token);
			}catch(IllegalArgumentException e){
				logger.error("Illegal Argument while fetching the userName !!");
				e.printStackTrace();
			}catch(ExpiredJwtException e) {
				logger.error("Given jwt token is expired !!");
				e.printStackTrace();
			}catch(MalformedJwtException e) {
				logger.error("Some changed has done in token !! Invalid Token");
				e.printStackTrace();
			}catch(Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}else {
			logger.info("Invalid Header Value !!");
		}
		
		if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
			Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
			if(validateToken) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));		
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				logger.info("Vlidation Fails !!");
			}
		}
		
		filterChain.doFilter(request, response);
	}
		
	

}
