package com.letsdoapps.depsio.maven.depsio_maven_plugin;

/*
 * Copyright (c) 2013 Jérémie BORDIER
 * 
 * MIT License
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo( name = "depsio", defaultPhase = LifecyclePhase.PACKAGE )
public class DepsIoMojo extends AbstractMojo
{

    @Parameter( property = "depsio.appkey", required = true )
    private String appkey;

    public void execute() throws MojoExecutionException {
    	getLog().info("");
    	
    	assert(new File("pom.xml").exists());
    	
        try {
        	getLog().info("--- Deps.io : Updating pom.xml (appkey : " + appkey + ") ---");
            // Send the request
            URL url = new URL("http://deps.io/api/v1/apps/" + appkey + "/update");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            
            String data = "type=pom&dependencies=" + URLEncoder.encode(readPom(), "UTF-8");
            //write parameters
            writer.write(data);
            writer.flush();
            
            // Get the response
            StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            writer.close();
            reader.close();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            	getLog().info("--- Deps.io : Upload ok. ---");
            } else {
            	getLog().warn("--- Deps.io : Error during upload ---");
            	getLog().warn(answer.toString());
            }
        } catch (Exception ex) {
        	getLog().warn("--- Deps.io : Error during upload : " + ex.getMessage() + " ---");
        	if (getLog().isDebugEnabled()) {
            	getLog().warn(ex);
        	}
        }
    	getLog().info("");
    }
    
    private String readPom() throws Exception {
    	String everything = "";
		BufferedReader br = new BufferedReader(new FileReader("pom.xml"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			everything = sb.toString();
		} finally {
			br.close();
		}
		return everything;
    }
}
