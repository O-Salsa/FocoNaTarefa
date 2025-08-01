package com.heilsalsa.foconatarefa.controller;

import org.springframework.web.bind.annotation.*;

import com.heilsalsa.foconatarefa.security.JwtUtil;

import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> loginData){
	String usuario = loginData.get("usuario");
	String senha = loginData.get("senha");
	
	
	//Autenticação simples (fixa) só pra mim <<<<<<<<<<
	
	
	if("admin".equals(usuario) && "admin".equals(senha)) {
		String token = JwtUtil.generateToken(usuario);
	
	Map<String, String> resposta = new HashMap<>();
	resposta.put("token", token);
	return ResponseEntity.ok(resposta);
	} else {
		Map<String, String> resposta = new HashMap<>();
		resposta.put("Erro", "Usuário ou senha inválidos");
		return ResponseEntity.status(401).body(resposta);
	}
	}
}
