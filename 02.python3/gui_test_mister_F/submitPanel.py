# -*- coding: utf-8 -*-
from tkinter import font
import tkinter
import re

"""
    submitPanel.py
    
    サブクラス SubmitPanel。
    送信ボタンパネル作成、各Action処理実装クラス。
    
    _2019/06/25 CREATE at per6_jp
    
"""

class SubmitPanel:
    submitBtn = None
    inputPanel = None
    answerPanel = None
    imagePanel = None
    
    dirpath = None
    csvMap = None
    
    def __init__(self, root, inputPanel, answerPanel, imagePanel, csvMap, dirpath):
        self.dirpath = dirpath
        self.csvMap = csvMap
        
        # 必要パネル格納
        self.inputPanel = inputPanel
        self.inputPanel.textArea.bind('<Return>', self.submitKeyword)   # エンターで送信処理
        self.answerPanel = answerPanel
        self.imagePanel = imagePanel
        
        # パネル作成
        self.submitBtn = tkinter.Button(root, text = "送信", command = self.submitKeyword)
        self.submitBtn.pack(fill = 'x', padx = 10, side = 'left')
    
    def submitKeyword(self, event = None):
        answerStr = ""
        textStr = self.inputPanel.textArea.get()
        
        # カナ→かなに修正
        textStr = "".join([chr(ord(ch) - 96) if ("ァ" <= ch <= "ヴ") else ch for ch in textStr])
        
        # 半角数字→全角数字に修正
        textStr = "".join([chr(ord(ch) + 65248) if ("0" <= ch <= "9") else ch for ch in textStr])
        
        # 促音を修正
        textStr = textStr.replace("ぁ", "あ")
        textStr = textStr.replace("ぃ", "い")
        textStr = textStr.replace("ぅ", "う")
        textStr = textStr.replace("ぇ", "え")
        textStr = textStr.replace("ぉ", "お")
        textStr = textStr.replace("っ", "つ")
        textStr = textStr.replace("ゃ", "や")
        textStr = textStr.replace("ゅ", "ゆ")
        textStr = textStr.replace("ょ", "よ")
        textStr = textStr.replace("ゎ", "わ")
        
        if re.compile(r'^[あ-んー０-９]+$').fullmatch(textStr):
            # 入力文字が全てひらがな判定されたなら結果描画
            if textStr in self.csvMap:
                answerStr = self.csvMap[textStr]['answer']
                self.imagePanel.setImage(self.dirpath + "/img/" + self.csvMap[textStr]['img_name'] + ".png")
            else:
                answerStr = self.csvMap['NOTHING']['answer']
                self.imagePanel.setImage(self.dirpath + "/img/" + self.csvMap['NOTHING']['img_name'] + ".png")
        else:
            answerStr = self.csvMap['ERROR']['answer']
            self.imagePanel.setImage(self.dirpath + "/img/" + self.csvMap['ERROR']['img_name'] + ".png")
        
        # 画面描画
        self.answerPanel.textAreaStrPanel.set(answerStr)
        self.imagePanel.showImage()
        self.inputPanel.textArea.delete(0, tkinter.END)
        