package com.nercl.music.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RandomCodeController {

	private int width = 85;
	private int height = 30;
	private int codeCount = 4;
	private int xx = 15;
	private int fontHeight = 20;
	private int codeY = 20;
	private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
	        'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	private BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	private Graphics gd = buffImg.getGraphics();

	@GetMapping("/code")
	public void getCode(HttpServletRequest req, HttpServletResponse resp) {
		this.setGraphics(gd);
		this.drawLine(gd);
		String randomCode = this.drawCode(gd);
		HttpSession session = req.getSession();
		session.setAttribute("code", randomCode);
		this.writeImageToResponse(buffImg, resp);
	}

	private void setGraphics(Graphics gd) {
		gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, width, height);
		Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
		gd.setFont(font);
		gd.setColor(Color.BLACK);
		gd.drawRect(0, 0, width - 1, height - 1);
		gd.setColor(Color.BLACK);
	}

	private void drawLine(Graphics gd) {
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			gd.drawLine(x, y, x + xl, y + yl);
		}
	}

	private String drawCode(Graphics gd) {
		Random random = new Random();
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;
		for (int i = 0; i < codeCount; i++) {
			String code = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			gd.setColor(new Color(red, green, blue));
			gd.drawString(code, (i + 1) * xx, codeY);
			randomCode.append(code);
		}
		return randomCode.toString();
	}

	private void writeImageToResponse(BufferedImage buffImg, HttpServletResponse resp) {
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);
		resp.setContentType("image/jpeg");
		ServletOutputStream sos;
		try {
			sos = resp.getOutputStream();
			ImageIO.write(buffImg, "jpeg", sos);
			sos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
