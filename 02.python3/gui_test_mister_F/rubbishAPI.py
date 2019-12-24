# -*- coding: utf-8 -*-
from mainFrame import MainFrame
import os

"""
    rubbishAPI.py
    
    メインクラス RubbishAPI。
    最初に起動する。
    
    _2019/06/25 CREATE at per6_jp
    
"""

class RubbishAPI:
    csvMap   = {}
    dirpath  = ""
    fileName = "mst/keywordCSV.csv"
    
    def __init__(self, window_w, window_h, title, dirpath):
        self.dirpath = dirpath
        self.fileName = dirpath + "/" + self.fileName
        
        # CSV読み込み
        self.csvMap = self.importCSV()
        
        # MainFrame呼び出し
        self.mainFrame = MainFrame(window_w, window_h, title, self.csvMap, self.dirpath)
        
    def show(self):
        self.mainFrame.run()
    
    ###
    # CSVを読込み辞書化する
    # 
    ###
    def importCSV(self):
        try:
            file = open(self.fileName, encoding='utf-8')
            lines = file.readlines()
            for line in lines:
                words = line.split(',')
                
                # おしゃべり辞書作成
                if len(words) == 3:
                    csv = {
                            'img_name' : words[1].rstrip('\n'),
                            'answer'   : words[2].rstrip('\n')
                        }
                    self.csvMap[words[0].rstrip('\n')] = csv
                
                # ERROR(かな以外エラー)設定
                if 'ERROR' not in self.csvMap:
                    csv = {
                            'img_name' : "000",
                            'answer'   : "すべて「かな」で　かいてください"
                        }
                    self.csvMap['ERROR'] = csv
                
                # NOTHING(辞書不在エラー)設定
                if 'NOTHING' not in self.csvMap:
                    csv = {
                            'img_name' : "000",
                            'answer'   : "たんごが　とうろく　されていません"
                        }
                    self.csvMap['NOTHING'] = csv
                
                # FIRST(初回表示文言)設定
                if 'FIRST' not in self.csvMap:
                    csv = {
                            'img_name' : "000",
                            'answer'   : "きーわーどを　いれてください"
                        }
                    self.csvMap['FIRST'] = csv
                
        except Exception as e:
            print(e)
        finally:
            file.close()
        
        return self.csvMap

api = RubbishAPI(300, 300, "RUBBISH", os.getcwd())
api.show()