<?php
/**
 * ログ出力クラス
 */
class LogUtil {
    
    /** ログファイル */
    private $logFileName;
    
    public function __construct() {
        
        # ログファイル生成
        $this -> createLog();
    }
    
    /** ログファイル生成 */
    public function createLog() {
        # ファイル名を設定(yyyyMMdd)
        $this -> logFileName = date("Ymd") . LOG_EXT;
        
        # ディレクトリを作成
        if(!file_exists(LOG_DIR)) {
            mkdir(LOG_DIR);
        }
        
        # INFOファイルを作成
        if(!file_exists(LOG_DIR . INFO_LOG . $this -> logFileName))
            touch(LOG_DIR . INFO_LOG . $this -> logFileName);
        # ERRファイルを作成
        if(!file_exists(LOG_DIR . ERR_LOG . $this -> logFileName))
            touch(LOG_DIR . ERR_LOG . $this -> logFileName);
    }
    
    /**
     * エラーログ出力
     */
    public function error($e) {
        $timestamp = date("Y/m/d H:i:s　");
        $file = fopen(LOG_DIR . ERR_LOG . $this -> logFileName, "a");
        @fwrite($file, $timestamp . $e . "\r\n");
        fclose($file);
    }
    
    /**
     * インフォログ出力
     */
    public function info($str) {
        $timestamp = date("Y/m/d H:i:s　");
        $file = fopen(LOG_DIR . INFO_LOG . $this -> logFileName, "a");
        @fwrite($file, $timestamp . $str . "\r\n");
        fclose($file);
    }
}