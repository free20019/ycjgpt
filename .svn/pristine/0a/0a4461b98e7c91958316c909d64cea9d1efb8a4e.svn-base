package helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class test {
	public static void main(String[] args) throws IOException {
//		Image[] array = new Image[10];
//		Image image = ImageIO.read(new File("C:\\\\Users\\Administrator\\Pictures\\yhyytj.png"));
//		array[0] = image;
////		BufferedImage im = 
////		BufferedImage image = ImageIO.read(new File("d:\\source.gif"));
//		ImageIO.write((RenderedImage) array[0], "PNG", new File("C:\\\\Users\\Administrator\\Pictures\\test.png"));
		
		File file = new File("C:\\\\Users\\Administrator\\Pictures\\yhyytj.png");
		byte[] bbb =  new byte[1024];
		try {
			InputStream a = new FileInputStream(file);
			a.read(bbb);
			System.out.println(Arrays.toString(bbb));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
