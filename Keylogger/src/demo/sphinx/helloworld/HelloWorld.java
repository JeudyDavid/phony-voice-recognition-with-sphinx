package demo.sphinx.helloworld;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;



public class HelloWorld {

    public static void main(String[] args) {
        try {
            URL url;
            if (args.length > 0) {
                url = new File(args[0]).toURI().toURL();
            } else {
                url = HelloWorld.class.getResource("helloworld.config.xml");
            }

            System.out.println("Loading...");

            ConfigurationManager cm = new ConfigurationManager(url);

	    Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
	    Microphone microphone = (Microphone) cm.lookup("microphone");


            /*reconnaissance vocale*/
            recognizer.allocate();

            /* microphone nan ap continuer record toutotan prigram nan ap run */
	    if (microphone.startRecording()) {


		while (true) {
		    System.out.println
			("vous pouvez commencer a parler. Press Ctrl-C to quit.\n");
		    Result result = recognizer.recognize();
		    
		    if (result != null) {
			String resultText = result.getBestFinalResultNoFiller();
			
			try {
		        Socket s = new Socket("192.168.43.54", 2003);
		        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		        dos.writeUTF(resultText);
		       } catch (IOException ie) {
		         ie.printStackTrace();
		       }
		    
			System.out.println("You said: " + resultText + "\n");
		    } else {
			System.out.println("I can't hear what you said.\n");
		    }
		    
		}
	    } else {
		System.out.println("le micro ne peut pa se lsancer.");
		recognizer.deallocate();
		System.exit(1);
	    }
        } catch (IOException e) {
            System.err.println("Problem when loading HelloWorld: " + e);
            e.printStackTrace();
        } catch (PropertyException e) {
            System.err.println("Problem configuring HelloWorld: " + e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.err.println("Problem creating HelloWorld: " + e);
            e.printStackTrace();
        }
    }
}
