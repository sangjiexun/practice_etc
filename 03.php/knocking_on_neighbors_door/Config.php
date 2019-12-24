<?php
/**
 * Config情報
 */

// ディレクトリパス
define('DIR_PATH', str_replace( '\\', '/', __DIR__ ) . '/');

// 訪問者パス
define('VISITOR_DIR', DIR_PATH . 'visitor/');

// 隣人パス
define('MIWA_PATH', VISITOR_DIR . 'owner/隣人.txt');

// ファイルエンコーディング
define('ENCODE', 'UTF-8');

// ログ用情報
define('LOG_DIR', DIR_PATH . 'log/');
define('INFO_LOG', 'info_');
define('ERR_LOG', 'err_');
define('LOG_EXT', '.log');