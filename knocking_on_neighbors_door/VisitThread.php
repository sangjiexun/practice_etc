<?php
/**
 * 隣人が訪問者を門前払いするだけのスレ
 */

require_once(DIR_PATH . 'Visitor.php');

class VisitThread {
    /** Commonツール */
    private $common;
    /** LogUtilツール */
    private $logUtil;
    /** 隣人 */
    private $name;
    /** ぶぶ漬けList */
    private $talkList;
    /** 捨てぜりふ */
    private $endText;
    /** 訪問者 */
    private $visitor;
    
    /**
     * インスタンス生成
     */
    public function __construct($common, $logUtil) {
        $this -> common = $common;
        $this -> logUtil = $logUtil;
        // 変数情報Map取得
        $personMap = $this -> common -> getPersonMap(MIWA_PATH, ENCODE);
        
        $this -> name = $personMap['name'];
        $this -> talkList = $personMap['talkList'];
        $this -> endText = $personMap['endText'];
        $this -> visitor = new Visitor($common);
        
        print($this -> visitor -> getName() . "が訪問しました\n");
        print($this -> name . "が対応します.\n");
    }
    
    /**
     * 愛のままにわがままに僕は君だけは家に上げない
     */
    public function run() {
        
        // 訪問者は暇をつぶしたい
        print("\n");
        $this -> visitor -> talk();
        $this -> talk();
        
        for($i = 1; $i < $this -> visitor -> getSatisfactionFlg(); $i++) {
            // 訪問者が満足するまで押し問答を繰り返す
            sleep(mt_rand(1, 3));
            print("\n");
            $this -> visitor -> talk();
            $this -> talk();
        }
        
        while(mt_rand(0, 1) === 0) {
            // 満足しても2ぶんの1の確率で奴は惰性で居座る
            sleep(mt_rand(1, 3));
            print("\n");
            $this -> visitor -> talk();
            $this -> talk();
        }
        
        // 2秒間の無言ののちようやく帰る
        sleep(2);
        print("\n");
        $this -> visitor -> endText();
        print($this -> name . ": " . $this -> endText);
        
        return;
    }
    
    /**
     * ぶぶ漬けListをランダムで表示
     */
    public function talk() {
        print($this -> name . ": " . $this -> talkList[mt_rand(0, count($this -> talkList) - 1)]);
    }
}