# -*- coding: utf-8 -*-
from tkinter import font
import tkinter

"""
    answerPanel.py
    
    サブクラス AnswerPanel。
    回答パネル作成、各Action処理実装クラス。
    
    ※_回答は全角20文字以内
    
    _2019/06/25 CREATE at per6_jp
    
"""

class AnswerPanel:
    textAreaFrm      = None
    textAreaStrPanel = None
    textAreaStrLabel = None
    textAreaStr      = None
    
    def __init__(self, root, textAreaStr):
        self.textAreaStr = textAreaStr
        
        # パネル作成
        self.textAreaFrm = tkinter.Frame(root, width = 280, height = 30)
        self.textAreaFrm.pack(side = tkinter.TOP)
        
        # 文字列パネル設定
        self.textAreaStrPanel = tkinter.StringVar()
        self.textAreaStrPanel.set(self.textAreaStr)
        textAreaFont = font.Font(family = 'Times', size = 10)
        
        # ラベル作成
        self.textAreaStrLabel = tkinter.Label(self.textAreaFrm, textvariable = self.textAreaStrPanel, 
                                    font = font.Font(family = 'Times', size = 10))
        self.textAreaStrLabel.pack(side = tkinter.TOP)