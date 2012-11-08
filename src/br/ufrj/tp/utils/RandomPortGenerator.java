package br.ufrj.tp.utils;

public class RandomPortGenerator {

	public static int generatePort()
	{
		int port;
		
		port = (int) Math.floor(4000*Math.random()) + 1200;

		if(port == 4444)
		{
			port = generatePort();
		}
		
		return port;
	}
}
