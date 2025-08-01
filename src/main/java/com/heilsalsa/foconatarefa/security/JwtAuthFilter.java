package com.heilsalsa.foconatarefa.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		
		
		//Permite livre acesso ao login e ao GET
		
		String path = req.getRequestURI();
		String method = req.getMethod();
		if (path.startsWith("/auth/login") || (path.startsWith("/api/tasks") && method.equals("GET"))) {
			chain.doFilter(request, response);
			return;
		}
		
		String header = req.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			res.getWriter().write("{\"erro\": \"Token ausente ou invalido\"}");
			return;
		}
		
		String token = header.substring(7);
		try {
			JwtUtil.getUsernameFromToken(token);
			chain.doFilter(request, response);
			} catch (Exception e) {
				res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				res.getWriter().write("{\"erro\": \"Token inválido ou expirado\"}");
				}
	}

}
