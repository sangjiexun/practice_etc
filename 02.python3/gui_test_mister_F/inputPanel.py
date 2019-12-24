# -*- coding: utf-8 -*-
from tkinter import font
import tkinter

"""
    inputPanel.py
    
    サブクラス InputPanel。
    入力フォームパネル作成、各Action処理実装クラス。
    
    _2019/06/25 CREATE at per6_jp
    
"""

class InputPanel:
    textFormFrm = None
    textArea    = None
    
    def __init__(self, root):
        
        # パネル作成
        self.textFormFrm = tkinter.Frame(root, width = 280, height = 30)
        self.textFormFrm.pack(fill = 'x', side = 'left')
        self.textArea = tkinter.Entry(self.textFormFrm, bd = 1, width = 20)
        self.textArea.pack(side = tkinter.TOP, pady = 15)