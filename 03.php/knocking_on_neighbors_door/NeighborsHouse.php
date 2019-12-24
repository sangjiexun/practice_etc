<?php
/**
 * ここがあの隣人のハウスね
 * ※実行ファイル
 */

include 'Config.php';
include DIR_PATH . 'Common.php';
include DIR_PATH . 'LogUtil.php';
require_once(DIR_PATH . 'VisitThread.php');

$logUtil = new LogUtil();
$common = new Common();

// 門前払いスレッドを作成
$visitThread = new VisitThread($common, $logUtil);
$logUtil -> info("門前払いスレッド作成完了");

// 門前払いスレッドを実施
$logUtil -> info("門前払いスレッド実施");
$visitThread -> run();
$logUtil -> info("門前払いスレッド終了");
