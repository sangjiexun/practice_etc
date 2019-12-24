# Analyzing_Image_Files_Practice_PJ
『画像ファイル解析練習プロジェクト』のGoogle訳  
このファイルはグローバル化のためのすべてをGoogle翻訳でまかなっています。  
また、趣味で作成しているため用途はなく、この文内には「したいこと」「使い方」「ここがダメ」の3つの内容しかありません。

## Purpose
- I want to convert an image file(BMP) to binary, and analyze.  
画像ファイル（ビットマップ）をバイナリ変換・解析したい。
  
- I want to redraw the analyzed image file in HTML using the Canvas tag.  
解析した画像ファイルをHTML上でCanvasタグを使用し再描画したい。

## Using
1. Compile "ImgToCanvas.java" and do the following:  
```java ImgToCanvas img\bmp.bmp```  
1. Converted HTML file and binary text are saved html Dir.
1. コンパイル、実行、HTMLファイルがクラス直下htmlフォルダにできる。

## So useless!!(write on Japanese)
### 出力ファイルの描画結果がおかしい。
何日か気合でやってみたけれどうまくいかない。  
バイナリからCanvas描画情報生成時、画像のタテ・ヨコ比率がおかしくなっているため発生しているのだと思われます。。  
ぶれぎみなのも何かしらのつくりが甘いからじゃないかと思います。。  
開発者は気が向いたらImgToCanvas.javaのcreateHTMLメソッドを調査し修正してください。

### ビットマップしか対応してない。
プロジェクト名と矛盾している。  
ビットマップ形式が一番読みやすいので、Canvas出力までの基盤を作ってから別の画像形式にも対応するつもりでした。  
ImgToCanvas.javaのgetImgFormatメソッドを作っているところを見るに、画像フォーマットを判定して変換対応させる気はあったのでしょう。
