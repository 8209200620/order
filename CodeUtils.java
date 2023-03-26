package edu.canteen.order.system.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * 验证码工具类
 *	动态的随机生成验证码
 */
public class CodeUtils {
	
	private static int WIDTH = 160;

    private static int HEIGHT = 45;
	
	private static Color getRandColor(int fc, int bc) {
		// 取其随机颜色
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	private static void drawRands(Graphics g, String rands)
    {
    	Random random = new Random();
        g.setColor(Color.BLACK);

        g.setFont(new Font(null, Font.ITALIC | Font.BOLD, 30));

        // 在不同的高度上输出验证码的每个字符

        g.drawString("" + rands.charAt(0), 20, 25+random.nextInt(10));

        g.drawString("" + rands.charAt(1), 50, 25+random.nextInt(10));

        g.drawString("" + rands.charAt(2), 80, 25+random.nextInt(10));

        g.drawString("" + rands.charAt(3), 120, 25+random.nextInt(10));


    }
	
	private static void drawBackground(Graphics g)
    {
        // 画背景
        g.setColor(getRandColor(0,255));

        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 随机产生 120 个干扰点

        for (int i = 0; i < 120; i++)
        {
            int x = (int) (Math.random() * WIDTH);

            int y = (int) (Math.random() * HEIGHT);

            int red = (int) (Math.random() * 255);

            int green = (int) (Math.random() * 255);

            int blue = (int) (Math.random() * 255);

            g.setColor(new Color(red, green, blue));

            g.drawOval(x, y, 1, 0);
        }
    }
	
	private  static String createRandom()
    {
        String str = "0123456789qwertyuiopasdfghjklzxcvbnm";

        char[] rands = new char[4];

        Random random = new Random();

        for (int i = 0; i < 4; i++)
        {
            rands[i] = str.charAt(random.nextInt(36));
        }

        return new String(rands);
    }
	
	
	 public static void createImageCode(HttpServletRequest request,HttpServletResponse response) throws Exception
	    {

	        // 设置浏览器不要缓存此图片
	        response.setHeader("Pragma", "no-cache");

	        response.setHeader("Cache-Control", "no-cache");

	        response.setDateHeader("Expires", 0);

	        String rands = createRandom();

	        BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
	                BufferedImage.TYPE_INT_RGB);

	        Graphics g = image.getGraphics();
	        // 产生图像
	        drawBackground(g);

	        drawRands(g, rands);

	        // 结束图像 的绘制 过程， 完成图像
	        g.dispose();

	        //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        HttpSession session = request.getSession();
	        
	        session.setAttribute("smsCode", rands);

	        ImageIO.write(image, "jpeg", response.getOutputStream());

	        //ByteArrayInputStream input = new ByteArrayInputStream(outputStream.toByteArray());


	        
	        //input.close();
	        
	        //outputStream.close();

	        response.getOutputStream().close();
	        
	    }
}
