<?php
/**
 * Commonツール
 * みんなでつかってね
 * なかよくつかってね
 */

class Common {
    
    /**
     * ファイルを読込み、名前、文言List、捨てゼリフ情報を返す
     */
    public function getPersonMap( $filePath = null, $encoding = null ) {
        $personMap = [];
        $name = '';
        $endText = "\n";
        $talkList = [];
        
        // ファイルを1行ごとに配列に格納する
        $f = file_get_contents( $filePath, $encoding );
        $f = str_replace( "\r", "", $f );      // Win用対策
        $f = explode( "\n", $f );
        
        for( $i = 0; $i < count($f); $i++ ) {
            if( $f[$i] !== "" ) {
                $talkList[] = $f[$i] . "\n";
            }
        }
        
        // $name取得
        $name = basename($filePath, '.txt');
        // $endText取得
        $endText = $talkList[count($talkList) - 1];
        // $talkList確定
        array_pop( $talkList );
        
        $personMap = [
            'name'     => $name,
            'endText'  => $endText,
            'talkList' => $talkList
        ];
        
        return $personMap;
    }
}