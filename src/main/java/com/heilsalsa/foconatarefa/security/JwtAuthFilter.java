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

        // Content-Type/charset para evitar "inv�lido"
        res.setContentType("application/json;charset=UTF-8");
        res.setCharacterEncoding("UTF-8");

        // Libera preflight (útil p/ front-end)
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            chain.doFilter(request, response);
            return;
        }  
        
        // Permite livre acesso ao login e ao GET
        String path = req.getRequestURI();
        String method = req.getMethod();
        
        // ✅ NÃO AUTENTICAR Swagger/OpenAPI nem a própria página
        if (path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")) {
            chain.doFilter(request, response);
            return;
        }
        
        if (path.startsWith("/auth/login")
                || isPublicGet(path, method)) {
            chain.doFilter(request, response);
            return;
        }

        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"erro\": \"Token ausente ou inválido\"}");
            return;
        }

        String token = header.substring(7);
        try {
            // Valida formato, assinatura e expiração (lança exceção se der ruim)
            JwtUtil.getUsernameFromToken(token);

            // Token ok -> segue o fluxo
            chain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"erro\": \"Token expirado\"}");
        } catch (io.jsonwebtoken.security.SignatureException e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"erro\": \"Assinatura do token inválida\"}");
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("{\"erro\": \"Token inválido ou malformado\"}");
        }
    }

    private boolean isPublicGet(String path, String method) {
        if (!"GET".equalsIgnoreCase(method)) {
            return false;
        }
        return "/api/tasks".equals(path) || "/api/tasks/".equals(path);
    }
}
