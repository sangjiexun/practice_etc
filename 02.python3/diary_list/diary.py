# -*- coding: utf-8 -*-

import os
import re
import random

"""
    diary.py
    
    mainクラス
    日記を読み込んで日付毎に表示させたりする
    させなかったりする
    
"""

class Diary:
    diaryMap = {}
    
    def __init__(self):
        self.carrentDir = os.getcwd().replace(os.path.sep, "/")
        print("--- Diary START ---")
    
    def run(self):
        print("--- RUN ---")
        self.read()
        
        print("PLEASE DATE (FORMAT: \"YYYYMMDD\")")
        
        key = ""
        while key != "exit":
            key = input("> PLEASE DATE SET KEY or \"rand\" or \"exit\": ")
            
            if key in self.diaryMap or key == "rand":
                if key == "rand":
                    # ランダムkey取得
                    key, value = random.choice(list(self.diaryMap.items()))
                
                print("【" , key , "】")
                
                # diaryMap[key]内のセクションをMap化参照する
                sectionMap = self.showSection(key)
                print();
                
                for sectionKey in sectionMap:
                    print("[", sectionKey, "]" + sectionMap[sectionKey][0])
                
                sectionKey = ""
                while sectionKey != "exit":
                    sectionKey = input(">> PLEASE NO SET KEY or \"exit\": ")
                    
                    if sectionKey in sectionMap:
                        print();
                        print("■_" + sectionMap[sectionKey][0])
                        print(sectionMap[sectionKey][1])
                        print("---")
                        for sectionKey in sectionMap:
                            print("[", sectionKey, "]" + sectionMap[sectionKey][0])
                
                print()
                print("PLEASE DATE (FORMAT: \"YYYYMMDD\")")
                
            elif key == "exit":
                print()
            else:
                print("NO DIARY IN " + key + ".")
        
        print("--- END ---")
    
    """
    " 内容を読み込んでリスト化する
    "
    """
    def showSection(self, dateKey):
        sectionMap = {}
        
        dateValue = self.diaryMap[dateKey]
        items = dateValue.splitlines()
        
        key = 0
        title = ""
        value = ""
        
        for item in items:
            # ・がでるまでは表示
            
            if re.compile(r"^・").match(item):
                if title != "":
                    sectionMap[str(key)] = [re.sub(r"^・", "", title), value]
                    key += 1
                    value = ""
                title = item
                
            else:
                if title != "":
                    value = value + item + "\n"
                else:
                    print(item)
        
        # 最後のMapを代入
        if title != "":
            sectionMap[str(key)] = [re.sub(r"^・", "", title), value]
            key += 1
            value = ""
            title = ""
        
        return sectionMap
    
    """
    " ファイルを読み込んでリスト化する
    " 
    """
    def read(self):
        try:
            key = ""
            value = ""
            
            # ファイルを読み込む
            print(self.carrentDir + "/diary/diary.txt")
            f = open(self.carrentDir + "/diary/diary.txt", "r", encoding = "utf-8")
            lines = f.readlines()
            for line in lines:
                line = line.replace("\n", "")
                
                if re.compile(r"■_[0-9]{4}\/[0-9]{2}\/[0-9]{2}").match(line):
                    if key != "":
                        self.diaryMap[key.replace("■_", "").replace("/","")] = value
                    key = line
                    value = ""
                else:
                    value = value + "\n" + line
            
            # 最後のMapを代入
            if key != "":
                self.diaryMap[key.replace("■_", "").replace("/", "")] = value
                key = ""
                value = ""
            
        except Exception as e:
            print(e)
        finally:
            f.close()

diary = Diary()
diary.run()