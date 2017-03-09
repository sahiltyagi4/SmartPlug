package com.iot.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.iot.databean.SocketState;
import com.iot.server.WebServer;

public class FetchCurrentSocketState extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int socket;
	SocketState socketState;
	PrintWriter writer;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		socket = Integer.parseInt(req.getParameter("socket"));
		
		socketState = WebServer.socketStates.get(socket);
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("previousState", socketState.getCurrentSocketState());
		
		writer = resp.getWriter();
		writer.println(jsonObject.toString());
	}	
}