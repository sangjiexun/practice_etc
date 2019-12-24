import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.lang.Exception;

import javax.imageio.ImageIO;

import java.nio.ByteBuffer;

import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 画像ファイルをバイナリ変換・解析し
 * HTML上でCanvasタグを使用し再描画するための
 * HTMLファイルを生成する
 */
class ImgToCanvas {
	
	public static void main(String[] args) {
		
		if(args.length > 0) {
			String imagePath = args[0];
			
			if(isFileExists(imagePath)) {
				
				byte[] bytes = null;
				try {
					// ファイルをバイナリ変換
					bytes = getImageToBytes(imagePath);
					
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				if(bytes != null) {
					// バイナリ情報解析
					try {
						// バイナリ情報を16進数に変換
						String[] hexes = binaryToHexes(bytes);
						
						// バイナリ情報から読み取ったフォーマットを取得
						String imgFormat = getImgFormat(hexes);
						
						if(imgFormat == null) {
							throw new Exception("仕様外のフォーマットです");
						}
						
						
						System.out.println(imgFormat + "の情報を解析します...");
						
						if(imgFormat.equals("bmp")) {
							// BMP画像の情報取得
							
							// ファイルサイズ取得
							int imageSize = getHexToInt(hexes, 2, 5, imgFormat);
							
							// 幅を取得
							int imageX = getHexToInt(hexes, 18, 21, imgFormat);
							
							// 高さを取得
							int imageY = getHexToInt(hexes, 22, 25, imgFormat);
							
							// 各画素値を取得
							String[][] rgbs = new String[imageY][imageX];
							
							List<String> rgbList = new ArrayList<String>();
							int index = 54;
							for(int i = 0; i < imageY * imageX; i++) {
								// rgb情報を1ピクセルずつ取得・格納
								int b = getHexToInt(hexes, index, index++, imgFormat);
								int g = getHexToInt(hexes, index, index++, imgFormat);
								int r = getHexToInt(hexes, index, index++, imgFormat);
								rgbList.add("rgb(" + r + ", " + g + ", " + b + ")");
							}
							// 取得情報順反転
							Collections.reverse(rgbList);
							
							index = 0;
							for(int y = 0; y < imageY; y++) {
								// BMPは取得順が逆のため、(0, 0)から順に取得し直す
								for(int x = imageX - 1; x >= 0; x--) {
									rgbs[y][x] = rgbList.get(index++);
								}
							}
							
							// 取得情報をもとにHTMLファイルを作成
							String htmlPath = createHTML(imageSize, imageX, imageY, rgbs);
							
							// px数増えると変なrgb値が取れてくるので検証中
							// とりあえずテキストにrgb値を出してみるだけ
							String htmlFilePath = "";
							PrintWriter pw = null;
							try {
								// ファイル名設定
								String dir = "html/";
								Calendar c = Calendar.getInstance();
								String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(c.getTime()) + ".txt";
								htmlFilePath = dir + fileName;
								
								File htmlFile = new File(htmlFilePath);
								
								if(!(new File(dir).exists())) {
									new File(dir).mkdir();
								}
								if(!isReadFile(htmlFile)) {
									htmlFile.createNewFile();
								}
								
								pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(htmlFile, true), "UTF-8"));
								
								for(String rgb : rgbList) {
									pw.println(rgb);
									pw.flush();
								}
								pw.flush();
								
								pw.close();
							} catch(Exception e) {
								e.printStackTrace();
							} finally {
								if(pw != null) {
									try {
										pw.close();
									} catch(Exception e) {
										e.printStackTrace();
									}
								}
							}
							
							if(htmlPath.length() > 0) {
								System.out.println("下記ファイルパスにてにHTMLファイルを生成しました");
								System.out.println(htmlPath);
							} else {
								System.out.println("HTMLファイルの生成に失敗しました");
							}
						}
						
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("ファイルのバイナリ化に失敗しました");
				}
				
			} else {
				// 引数に指定したファイルが存在しない
				System.out.println("ファイルが存在しないか読み込めません");
			}
		} else {
			// 引数が存在しない
			System.out.println("引数 [画像ファイルパス] が設定されていません");
		}
		
		// HTMLファイル保存場所を設定
		String htmlPath = "./html/";
		
	}
	
	/**
	 * ファイル存在チェック
	 */
	public static boolean isFileExists(String filePath) {
		boolean bool = false;
		File file = new File(filePath);
		
		if(file.exists() && file.canRead()) {
			// filePathが存在し読み込み可能ならtrue
			bool = true;
		}
		
		return bool;
	}
	
	/**
	 * 指定した画像ファイルをバイナリ変換
	 */
	public static byte[] getImageToBytes(String filePath) {
		byte[] bytes = null;
		
		String format = "bmp";
		int point = filePath.lastIndexOf(".");
		if(point != -1) {
			// ファイルパスから拡張子を取得
			format = filePath.substring(point + 1);
			format = format.toLowerCase();
			if(format.equals("jpeg")) { format = "jpg"; }
		}
		
		// if(format.equals("bmp") || format.equals("png") || format.equals("jpg") || format.equals("gif")) {
		if(format.equals("bmp")) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				File file = new File(filePath);
				BufferedImage bi = ImageIO.read(file);
				ImageIO.write(bi, format, baos);
				bytes = baos.toByteArray();
			} catch(IOException e) {
				return null;
			}
		}
		
		return bytes;
	}
	
	/**
	 * バイナリ情報byte[]を16進数文字列hexes[]に変換し返す
	 */
	public static String[] binaryToHexes(byte[] bytes) {
		String[] hexes = new String[bytes.length];
		
		for(int i = 0; i < bytes.length; i++) {
			hexes[i] = getHexCode(bytes[i]);
		}
		
		return hexes;
	}
	
	/**
	 * Javaのバイナリ文字は符号が付くので、
	 * 1byteを符号を除去した16進数の文字列で返す
	 */
	public static String getHexCode(byte b) {
		String hexCode = "0" + Integer.toHexString(b & 0xFF).toLowerCase();
		hexCode = hexCode.substring(hexCode.length() - 2);
		return hexCode;
	}
	
	/**
	 * 画像フォーマット情報を取得
	 */
	public static String getImgFormat(String[] hexes) {
		String str = "";
		for(int i = 0; i < 2; i++) { str += hexes[i]; }
		
		if(str.equals("424d")) {	// BMP
			return "bmp";
		}
		
		if(str.equals("ffd8")) {	// JPG
			return "jpg";
		}
		
		for(int i = 2; i < 4; i++) { str += hexes[i]; }
		if(str.equals("47494638")) {	// GIF
			return "gif";
		}
		
		for(int i = 4; i < 8; i++) { str += hexes[i]; }
		if(str.equals("89504e470d0a1a0a")) {	// PNG
			return "png";
		}
		
		return null;
	}
	
	/**
	 * startIntからendIntまでの文字を取得し、int型に変換し返す
	 */
	public static int getHexToInt(String[] hexes, int startInt, int endInt, String format) {
		String strs[] = new String[endInt - startInt + 1];
		int strsIndex = 0;
		
		// 指定範囲の16進数文字列を取得
		for(int i = startInt; i <= endInt; i++) {
			strs[strsIndex++] = hexes[i];
		}
		
		// 1文字列として格納
		String hexSize = "";
		if(format.equals("bmp")) {
			// BMP文字列の場合、リトルエンディアン方式で記述されているため
			// 終点から始点の順に値を設定する
			for(int i = strs.length - 1; i >= 0; i--) {
				hexSize += strs[i];
			}
		} else {
			for(int i = 0; i < strs.length; i++) {
				hexSize += strs[i];
			}
		}
		
		// 文字列を数値変換する
		int result = Integer.parseInt(hexSize, 16);
		
		return result;
	}
	
	/**
	 * 取得情報をもとにCanvasタグをもつHTMLファイルを生成する
	 */
	public static String createHTML(int imageSize, int imageX, int imageY, String[][] rgbs) {
		List<String> htmlList = new ArrayList<String>();
		
		htmlList.add("<! doctype html>");
		htmlList.add("<html lang=\"ja\">");
		htmlList.add("  <head>");
		htmlList.add("    <meta charset=\"UTF-8\" />");
		htmlList.add("    <title>Image Canvas</title>");
		htmlList.add("  </head>");
		htmlList.add("  <body style=\"margin: 1em;\">");
		htmlList.add("    ");
		htmlList.add("    <canvas id=\"imageCanvas\" width=\"" + imageX + "\" height=\"" + imageY + "\"></canvas>");
		htmlList.add("    ");
		htmlList.add("    <div>");
		htmlList.add("      <p><strong>ファイルサイズ</strong>: " + imageSize + "bytes</br>");
		htmlList.add("      <strong>画像サイズ</strong>: " + imageX + " * " + imageY + "(px)</br>");
		htmlList.add("    </div>");
		htmlList.add("    ");
		htmlList.add("    <script>");
		htmlList.add("      onload = function() {");
		htmlList.add("        draw();");
		htmlList.add("      };");
		htmlList.add("      function draw() {");
		htmlList.add("        /* canvas要素のノードオブジェクト */");
		htmlList.add("        var canvas = document.getElementById('imageCanvas');");
		htmlList.add("        /* canvas要素の存在チェックとCanvas未対応ブラウザの対処 */");
		htmlList.add("        if(!canvas || !canvas.getContext) {");
		htmlList.add("          return false;");
		htmlList.add("        }");
		htmlList.add("        /* 2Dコンテキスト */");
		htmlList.add("        var ctx = canvas.getContext('2d');");
		htmlList.add("        ");
		htmlList.add("        var rgbArgs = [");
		
		// このあたりがおかしい
		// rgbsの取得がおかしい？
		// ここのループ処理がおかしい？
		// エターなってるので気が向いた時に考える
		for(int h = 0; h < imageY; h++) {
			String str = "          [";
			for(int w = 0; w < imageX; w++) {
				str += "'" + rgbs[h][w] + "'";
				if(w < rgbs[h].length - 1) {
					str += ", ";
				}
			}
			if(h < rgbs.length - 1) {
				str += "],";
			} else {
				str += "]";
			}
			htmlList.add(str);
		}
		System.out.println("h: " + rgbs.length);
		System.out.println("w: " + rgbs[0].length);
		
		htmlList.add("        ];");
		htmlList.add("        ");
		htmlList.add("        /* 点を描く */");
		htmlList.add("        drawCanvas(ctx, rgbArgs);");
		htmlList.add("      };");
		htmlList.add("      ");
		htmlList.add("      function drawCanvas(ctx, rgbArgs) {");
		htmlList.add("        for(var h = 0; h < rgbArgs.length; h++) {");
		htmlList.add("          for(var w = 0; w < rgbArgs[h].length; w++) {");
		htmlList.add("            ctx.beginPath();");
		htmlList.add("            ctx.moveTo(w, h);");
		htmlList.add("            ctx.lineTo(w + 1, h + 1);");
		htmlList.add("            ctx.strokeStyle = rgbArgs[h][w];");
		htmlList.add("            ctx.closePath();");
		htmlList.add("            ctx.stroke();");
		htmlList.add("          }");
		htmlList.add("        }");
		htmlList.add("      };");
		
		htmlList.add("    </script>");
		htmlList.add("  </body>");
		htmlList.add("</html>");
		
		// HTMLファイル作成
		String htmlFilePath = "";
		PrintWriter pw = null;
		try {
			// ファイル名設定
			String dir = "html/";
			Calendar c = Calendar.getInstance();
			String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(c.getTime()) + ".html";
			htmlFilePath = dir + fileName;
			
			File htmlFile = new File(htmlFilePath);
			
			if(!(new File(dir).exists())) {
				new File(dir).mkdir();
			}
			if(!isReadFile(htmlFile)) {
				htmlFile.createNewFile();
			}
			
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(htmlFile, true), "UTF-8"));
			
			for(String html : htmlList) {
				pw.println(html);
				pw.flush();
			}
			pw.flush();
			
			pw.close();
		} catch(Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if(pw != null) {
				try {
					pw.close();
				} catch(Exception e) {
					e.printStackTrace();
					return "";
				}
			}
		}
		
		return System.getProperty("user.dir") + "/" + htmlFilePath;
	}
	
	/** 
	 * ファイルが存在すればtrue, 存在しなければfalseを返す
	 */
	public static boolean isReadFile(File file) {
		if(file.exists()) {
			if(file.isFile() && file.canRead()) {
				return true;
			}
		}
		return false;
	}
}