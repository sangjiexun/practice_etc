<?php
/**
 * ろくな訪問者がいない
 */

class Visitor {
    /** Commonツール */
    private $common;
    /** 訪問者 */
    private $name;
    /** からかい文言List */
    private $talkList;
    /** 捨てぜりふ */
    private $endText;
    /** 満足度フラグ(3～10) */
    private $satisfactionFlg = 3;
    
    /**
     * インスタンス生成
     */
    public function __construct($common) {
        $this -> common = $common;
        // 変数情報Mapをランダムに取得
        $personMap = $this -> getPersonMap(VISITOR_DIR);
        
        $this -> name = $personMap['name'];
        $this -> talkList = $personMap['talkList'];
        $this -> endText = $personMap['endText'];
        $this -> satisfactionFlg = mt_rand(3, 10);
    }
    
    /**
     * からかい文言Listをランダムで表示
     */
    public function talk() {
        print($this -> name . ": " . $this -> talkList[mt_rand(0, count($this -> talkList) - 1)]);
    }
    
    /**
     * 飽きたんで帰ります
     */
    public function endText() {
        print($this -> name . ": " . $this -> endText);
    }
    
    /**
     * 指定ディレクトリ内からランダムにファイルを選択し、訪問者情報をGET!
     */
    public function getPersonMap($dir) {
        $personMap = [];
        $fileList = [];
        
        if( is_dir( $dir ) && $handle = opendir( $dir ) ) {
            while( ($file = readdir( $handle )) !== false ) {
                if( filetype( $path = $dir . $file ) == "file" ) {
                    // ファイルパス一覧をListに格納
                    $fileList[] = $path;
                }
            }
        }
        
        // 訪問者テキストファイルからランダムに1件選択しpersonMapを作成
        $filePath = $fileList[mt_rand(0, count($fileList) - 1)];
        $personMap = $this -> common -> getPersonMap($filePath, ENCODE);
        
        return $personMap;
    }
    
    /**
     * どこぞで使われているゲッタ
     */
    public function getName() {
        return $this -> name;
    }
    public function getSatisfactionFlg() {
        return $this -> satisfactionFlg;
    }
}