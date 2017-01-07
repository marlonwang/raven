package net.logvv.raven.utils;

import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//pick from kangaroo-original
public class ImageUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

	/**
	 * @param imageBytes
	 *            图片的字节数组
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @param algor
	 *            缩放处理
	 * @param imageFormatName
	 *            图片格式
	 * @return
	 */
	public static byte[] scaleImage(byte[] imageBytes, int width, int height,
			int algor, String imageFormatName) {
		if (imageBytes == null)
			return null;

		BufferedImage image = null;// 
		BufferedImage newImage = null;
		ByteArrayOutputStream out = null;
		try {
			image = ImageIO.read(new ByteArrayInputStream(imageBytes));
			if (StringUtils.isBlank(imageFormatName)) {
				imageFormatName = getImageFormatName(imageBytes);
			}
			// BufferedImage.TYPE_INT_RGB
			// BufferedImage.TYPE_4BYTE_ABGR
			newImage = new BufferedImage(width, height, image.getType());
			newImage.getGraphics().drawImage(
					image.getScaledInstance(width, height, algor), 0, 0, null);
			out = new ByteArrayOutputStream();
			ImageIO.write(newImage, imageFormatName, out);
		} catch (Exception e) {
			logger.error("scaleImage faild!", e);
			throw new IllegalStateException("scaleImage faild!", e);
		} finally {
			try {
				if (image != null)
					image = null;
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
			}
		}
		return out.toByteArray();
	}

	/**
	 * 灏嗗浘鐗囦互width涓哄噯杩涜绛夋瘮缂╂斁
	 * 
	 * @param imageBytes
	 * @param width
	 * @param imageFormatName
	 * @return
	 */
	public static byte[] scaleImageByWidth(byte[] imageBytes, int width,
			String imageFormatName) {
		Image img = null;
		int height = 0;
		try {
			img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			int old_w = img.getWidth(null);
			int old_h = img.getHeight(null);
			if (old_h > 0 && old_w > 0) {
				// 浠ュ搴︿负鍑嗭紝璁＄畻楂樺害 绛夋瘮缂╂斁
				height = (int) ((float) width / old_w * old_h);
			}
		} catch (Exception e) {
			logger.error("scaleImageByWidth faild!", e);
		}
		return ImageUtils.scaleImage(imageBytes, width, height,
				Image.SCALE_SMOOTH, imageFormatName);
	}

	/**
	 * 灏嗗浘鐗囦互height涓哄噯杩涜绛夋瘮缂╂斁銆�
	 * 
	 * @param imageBytes
	 * @param height
	 * @param imageFormatName
	 * @return
	 */
	public static byte[] scaleImageByHeight(byte[] imageBytes, int height,
			String imageFormatName) {
		Image img = null;
		int width = 0;
		try {
			img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			int old_w = img.getWidth(null);
			int old_h = img.getHeight(null);
			if (old_h > 0 && old_w > 0) {
				// 浠ラ珮搴︿负鍑嗭紝璁＄畻瀹藉害 绛夋瘮缂╂斁
				width = (int) ((float) height / old_h * old_w);
			}
		} catch (Exception e) {
			logger.error("scaleImageByHeight faild!", e);
		}
		return ImageUtils.scaleImage(imageBytes, width, height,
				Image.SCALE_SMOOTH, imageFormatName);
	}

	/**
	 * 瑁佸壀鍥剧墖
	 * 
	 * @param imageBytes
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 * @param imageFormatName
	 * @return
	 */
	public static byte[] sliceImage(byte[] imageBytes, int left, int top,
			int width, int height, String imageFormatName) {
		if (imageBytes == null)
			return null;

		BufferedImage image = null;// 璇诲彇鍘熷浘
		BufferedImage newImage = null;
		ByteArrayOutputStream out = null;
		try {
			// 鍖哄煙鏈夋晥鎬у鐞�
			image = ImageIO.read(new ByteArrayInputStream(imageBytes));
			if (StringUtils.isBlank(imageFormatName)) {
				imageFormatName = getImageFormatName(imageBytes);
			}
			int old_width = image.getWidth(null);
			int old_height = image.getHeight(null);
			if (top > old_height)
				top = 0;
			if (left > old_width)
				left = 0;
			if (top + height > old_height)
				height = old_height - top;
			if (left + width > old_width)
				width = old_width - left;

			newImage = image.getSubimage(left, top, width, height);
			out = new ByteArrayOutputStream();
			ImageIO.write(newImage, imageFormatName, out);
		} catch (Exception e) {
			logger.error("sliceImage faild!", e);
			throw new IllegalStateException("sliceImage faild!", e);
		} finally {
			try {
				if (image != null)
					image = null;
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
			}
		}
		return out.toByteArray();
	}

	/**
	 * 鎴彇鍥剧墖
	 * 
	 * @param imgdata
	 *            鍘熷浘
	 * @param left
	 *            璧风偣宸﹀潗鏍�
	 * @param top
	 *            璧风偣涓婂潗鏍�
	 * @param width
	 *            鎴彇鐨勫搴�
	 * @param height
	 *            鎴彇鐨勯珮搴�
	 * @return
	 */
	// public static byte[] sliceImage(byte[] imgdata, int left, int top,
	// int width, int height) {
	// if (imgdata == null)
	// return null;
	//
	// Image image = null;// 璇诲彇鍘熷浘
	// ByteArrayOutputStream out = null;
	// BufferedImage newImage = null;
	// ImageReader reader = null;
	// try {
	// // 鍖哄煙鏈夋晥鎬у鐞�
	// ByteArrayInputStream bin = new ByteArrayInputStream(imgdata);
	// image = ImageIO.read(bin);
	// int old_width = image.getWidth(null);
	// int old_height = image.getHeight(null);
	// if (top > old_height)
	// top = 0;
	// if (left > old_width)
	// left = 0;
	// if (top + height > old_height)
	// height = old_height - top;
	// if (left + width > old_width)
	// width = old_width - left;
	//
	// ImageInputStream temp = ImageIO
	// .createImageInputStream(new ByteArrayInputStream(imgdata));//
	// 鍒涘缓鍥剧墖娴佽鍙栧櫒瀵硅薄鍒楄〃锛岀敤浜庤鍙栨煇涓�被鍥剧墖
	// reader = ImageIO.getImageReaders(temp).next();//
	// 鍥犱负鍙鐞嗕竴寮犲浘锛屾墍浠ュ彧鑾峰彇绗竴涓狪mageReader
	// reader.setInput(temp, true);// 璁剧疆鍥剧墖鏁版嵁娴�
	// ImageReadParam param = reader.getDefaultReadParam();//
	// Rectangle rect = new Rectangle(left, top, width, height);
	// param.setSourceRegion(rect);// 璁剧疆reader鍙傛暟
	//
	// newImage = reader.read(0, param);// 璇诲彇閮ㄥ垎鍥剧墖鏁版嵁
	// out = new ByteArrayOutputStream();
	// ImageIO.write(newImage, "jpeg", out);
	// } catch (Exception e) {
	// // log.error("鍥剧墖鎴彇锛�,e);
	// throw new IllegalStateException("鍥剧墖鎴彇锛�, e);
	// } finally {
	// try {
	// if (image != null)
	// image = null;
	// if (out != null) {
	// out.flush();
	// out.close();
	// }
	// } catch (Exception e) {
	// }
	// }
	// return out.toByteArray();
	// }

	/**
	 * 图片旋转
	 * 
	 * @param imageBytes
	 *            要处理的图片字节数组
	 * @param degree
	 *            旋转角度0/90/180/720
	 * @return
	 */
	public static byte[] rotateImage(byte[] imageBytes, final int degree,
			String imageFormatName) {
		BufferedImage image = null;// 
		BufferedImage newImage = null;
		ByteArrayOutputStream out = null;

		try {
			image = ImageIO.read(new ByteArrayInputStream(imageBytes));
			if (StringUtils.isBlank(imageFormatName)) {
				imageFormatName = getImageFormatName(imageBytes);
			}

			int w = image.getWidth();
			int h = image.getHeight();
			// int type = bufferedimage.getColorModel().getTransparency();//
			// 鍥剧墖妯″紡
			// BufferedImage img=new BufferedImage(h, w, type);
			// Graphics2D graphics2d=null;
			// // (graphics2d = (img = new BufferedImage(w, h, type))
			// // .createGraphics()).setRenderingHint(
			// // RenderingHints.KEY_INTERPOLATION,
			// // RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			// (graphics2d = (bufferedimage.createGraphics())).setRenderingHint(
			// RenderingHints.KEY_INTERPOLATION,
			// RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			//
			// graphics2d.rotate(Math.toRadians(degree), w/2,
			// h/2);//灏嗗潗鏍囧師鐐圭Щ鑷充腑蹇冿紝鐒跺悗鏃嬭浆鎸囧畾瑙掑害锛屾渶鍚庢仮澶嶅師鐐�
			// graphics2d.drawImage(img, 0, 0, null);//灏嗘棆杞悗鐨勫浘鐗囧～鍏呭埌bufferimage
			// graphics2d.dispose();

			AffineTransform affineTransform = new AffineTransform();
			if (degree == 180) {
				affineTransform.translate(w, h);
				newImage = new BufferedImage(w, h, image.getType());
			} else if (degree == 90) {
				affineTransform.translate(h, 0);
				newImage = new BufferedImage(h, w, image.getType());
			} else if (degree == 270) {
				affineTransform.translate(0, w);
				newImage = new BufferedImage(h, w, image.getType());
			}
			RenderingHints hints = new RenderingHints(
					RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			affineTransform.rotate(java.lang.Math.toRadians(degree));
			AffineTransformOp affineTransformOp = new AffineTransformOp(
					affineTransform, hints);
			affineTransformOp.filter(image, newImage);

			out = new ByteArrayOutputStream();
			ImageIO.write(newImage, imageFormatName, out);
		} catch (Exception e) {
			logger.error("rotateImage faild!", e);
			throw new IllegalStateException("rotateImage faild!", e);
		} finally {
			try {
				if (image != null)
					image = null;
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
			}
		}
		return out.toByteArray();
	}

	private static String getImageFormatName(final byte[] imageBytes)
			throws IOException {
		ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);
		ImageInputStream imageInput = ImageIO.createImageInputStream(input);
		Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInput);
		String imageFormatName = null;
		if (iterator.hasNext()) {
			ImageReader reader = iterator.next();
			imageFormatName = reader.getFormatName().toUpperCase();
		}

		try {
			return imageFormatName;
		} finally {
			if (imageInput != null) {
				imageInput.close();
			}
		}
	}

	// /**
	// * 鍥剧墖鍙犲姞
	// * @param imgobject 闇�鍚堝苟鐨勫浘鐗囨暟缁勶紝浠庡簳鍥惧線涓婁緷娆℃帓鍒�
	// * @param width 鍚堝苟鍚庣殑鍥剧墖瀹藉害
	// * @param height 鍚堝苟鍚庣殑鍥剧墖楂樺害
	// * @return
	// */
	// public static byte[] mergeImg(Images imgobject[],int width,int height){
	// try {
	// BufferedImage tag = new BufferedImage(width, height,
	// BufferedImage.TYPE_4BYTE_ABGR_PRE);
	// ByteArrayOutputStream bout=new ByteArrayOutputStream();
	// Graphics2D g=null;
	// for (int z = 0; z < imgobject.length; z++) {
	// // g2d[z] = tag.createGraphics();
	// g = tag.createGraphics();
	// // System.out.println("鍒涘缓绗� + z + "涓敾绗旓紒");
	// g.drawImage(imgobject[z].getBimg(), imgobject[z].getX(),
	// imgobject[z].getY(), imgobject[z].getBimg().getWidth(),
	// imgobject[z].getBimg().getHeight(), null);
	// }
	//
	// // System.out.println("鍚堟垚瀹屾瘯锛屽噯澶囪緭鍑猴紝璇风◢鍊檦~~~~~");
	// ImageIO.write(tag, "png", bout);
	// return bout.toByteArray();
	// } catch (Exception e) {
	// logger.error("", e);
	// }
	// return null;
	// }
	// /**
	// * 鍥剧墖鍙犲姞
	// * @param imgobject 闇�鍚堝苟鐨勫浘鐗囨暟缁勶紝浠庡簳鍥惧線涓婁緷娆℃帓鍒�
	// * @param width 鍚堝苟鍚庣殑鍥剧墖瀹藉害
	// * @param height 鍚堝苟鍚庣殑鍥剧墖楂樺害
	// * @param outfile 鍚堝苟鍚庣殑鍥剧墖瀛樺偍璺緞
	// * @return
	// */
	// public static void mergeImg(Images imgobject[],int width,int
	// height,String outfile){
	// byte[] imgs=mergeImg(imgobject, width, height);
	// if(imgs!=null&&!CommonUtil.isNull(outfile))
	// {
	// try {
	//
	// FileUtils.writeByteArrayToFile(new File(outfile), imgs);
	// } catch (Exception e) {
	// logger.error("", e);
	// }
	// }
	// }
	// public static void main(String[] args) {
	// try {
	// // byte[]dd= FileUtils.readBytes("/home/yuxiaowei/test/unnamed.png");
	// //
	// // byte[] data=scaleImg(dd, 70, 70, true);
	// //
	// // FileUtils.write("/home/yuxiaowei/test/unnamed_1.png", data);
	//
	// Long star = System.currentTimeMillis();
	// String src[] = new String[3];//鍥剧墖鐨勬湰鍦拌矾寰�
	// src[0] = "/home/yuxiaowei/test/1.png";// 搴曡竟
	// src[1] = "/home/yuxiaowei/test/unnamed_1.png";// 姝ｈ韩
	// src[2] = "/home/yuxiaowei/test/download.png";// 鍥炬爣
	// int x[] = new int[3];// 瀛樻斁X杞村潗鏍囩殑鏁扮粍
	// x[0] = 0;
	// x[1] = 7;
	// x[2] = 0;
	// int y[] = new int[3];// 瀛樻斁Y杞村潗鏍囩殑鏁扮粍
	// y[0] = 0;
	// y[1] = 7;
	// y[2] = 0;
	// Images[] imgo = new Images[src.length];// 鎵归噺鐢熸垚鍥剧墖瀵硅薄
	// for (int z = 0; z < imgo.length; z++) { // 鏋勯�鏂规硶娴嬪弬鏁版槸X,Y杞寸殑浣嶇疆鍜屾暟鎹�
	// imgo[z] = new Images(x[z], y[z], FileUtils.readFileToByteArray(new
	// File(src[z])));
	// }
	// mergeImg(imgo,84,84, "/home/yuxiaowei/test/out.png");// 鍚堟垚鍥剧墖鐨勬柟娉�
	// Long end = System.currentTimeMillis();
	// System.out.println("鍥剧墖鍚堟垚鑰楁椂" + (end - star) + "姣");
	//
	//
	//
	//
	// // String sourceFileName="/home/yuxiaowei/download/unnamed.png";
	// // String destFile2="/home/yuxiaowei/download/unnamed_1.jpg";
	//
	// // byte[] dd=FileUtils.readBytes(sourceFileName);
	// // byte[] logodata=FileUtils.readBytes(destFile2);
	// // long start=System.currentTimeMillis();
	// // resize(sourceFileName, destFile, 480,800);
	// // byte[] data=resize(dd, 320,240,95,"jpeg");
	// // byte[] data=scaleImg(dd, 320,240);
	// // byte[] data=cutImg(dd, 96, 96, 150, 150,"png");
	// // Point pos=new Point();
	// // pos.setLocation(50,50);
	// //// byte[] data=createWaterPrintByText(dd, "鍝堝搱test", pos, 20,
	// "png");//娣诲姞姘村嵃
	// // byte[] data=createWaterPrintByImg(dd, logodata, pos);
	// // FileUtils.write(destFile1, data);
	//
	// // byte[] data=resize(dd, 140,180,80,"jpeg");
	// // byte[] data=scaleImg(dd, 84, 84, false);
	// // byte[] data=scaleImg(dd, 120,180);
	// // FileUtils.write(destFile2, data);
	//
	//
	//
	// // data=resize(dd, 1024,500,95,"jpeg");
	// // data=scaleImg(dd, 1024,500);
	// // FileUtils.write(destFile3, data);
	// // byte[] dd=FileUtils.readBytes(sourceFileName);
	// // byte[] dd1=scaleImage(dd, 480,800,Image.SCALE_SMOOTH,true);
	// // FileUtils.write(destFile, dd1);
	//
	// // byte[]
	// d=HttpUtils.getBytes("http://gamecache.3g.cn/game/uploads/cutpic/201205/246416/fdc75891_s480.jpg");
	// // System.out.println(d.length);
	// // d=rotateImage("jpg", d, 90);
	// // FileUtils.write("d:/download/testrotate.jpg", d);
	// // long end=System.currentTimeMillis();
	// // System.out.println("times="+(end-start));
	// // HttpUtils.shutdown();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	
//	public static void main(String[] args) {
//		String filePath = "D:\\tmp\\F24Y49X9KJU2.jpg";
//		String fileNew = "D:\\tmp\\F24Y49X9KJU2_RESIZE.png";
//		int top = 5;
//		int left = 10;
//		int width = 540;
//		int height = 380;
//		
//		File file = new File(filePath);
//		
//		try {
//			byte[] imageByte = FileUtils.readFileToByteArray(file);
//			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageByte));
//			
//			// byte[] fileBytes = sliceImage(FileUtils.readFileToByteArray(file), left, top, width, height, "");
//			//FileUtils.writeByteArrayToFile(new File(fileNew), fileBytes);
//			
//			byte[] fileBytes = sliceImage(FileUtils.readFileToByteArray(file), 
//					0, 
//					0,
//					image.getWidth(),
//					Math.round(image.getHeight() * 0.4f),
//					"");
//			
//			FileUtils.writeByteArrayToFile(new File(fileNew), fileBytes);
//			
//			System.out.println("done");	// OK
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}

}

