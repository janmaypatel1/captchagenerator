package captcha;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CaptchaApplet extends Applet {
	  private ImageCaptchaGenerator captchaGenerator;
	    private TextField captchaInput;
	    private Button verifyButton;
	    private Button refreshButton;

	    @Override
	    public void init() {
	        captchaGenerator = new ImageCaptchaGenerator();
	        captchaInput = new TextField(10);
	        verifyButton = new Button("Verify");
	        refreshButton = new Button("Refresh");

	        add(captchaGenerator);
	        add(captchaInput);
	        add(verifyButton);
	        add(refreshButton);

	        verifyButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                String userInput = captchaInput.getText();
	                if (userInput.equalsIgnoreCase(captchaGenerator.getCaptchaText())) {
	                    showStatus("CAPTCHA verified successfully!");
	                } else {
	                    showStatus("Incorrect CAPTCHA! Please try again.");
	                }
	            }
	        });

	        refreshButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                captchaGenerator.refreshCaptcha();
	                repaint();
	            }
	        });
	    }

	    @Override
	    public void paint(Graphics g) {
	        captchaGenerator.paint(g);
	    }
	}
