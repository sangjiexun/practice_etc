# -*- coding: utf-8 -*-
from tkinter import font
import tkinter
import sys

from imagePanel import ImagePanel
from answerPanel import AnswerPanel
from inputPanel import InputPanel
from submitPanel import SubmitPanel

"""
    mainFrame.py
    
    サブクラス MainFrame。
    GUIパネル作成、各パネル設置クラス。
    
    _2019/06/25 CREATE at per6_jp
    
"""

class MainFrame:
    # メインフレーム変数
    root    = None
    menubar = None
    menu    = None
    
    # 各種パネル変数
    imagePanel  = None
    answerPanel = None
    inputPanel  = None
    submitPanel = None
    
    # CSV辞書
    dirpath = None
    csvMap = None
    
    def __init__(self, window_w, window_h, title, csvMap, dirpath):
        self.dirpath = dirpath
        self.csvMap = csvMap
        
        # ウインドウ初期設定
        self.root = tkinter.Tk()
        self.root.title(title)
        self.root.geometry("{0}x{1}+50+10".format(window_w, window_h))
        self.root.resizable(0, 0)
        
        # メニューバー
        self.menubar = tkinter.Menu(self.root)
        self.menu = tkinter.Menu(self.menubar, tearoff = 0)
        self.menu.add_command(label = "お前を消す方法", command = self.exit)
        self.menubar.add_cascade(label = "メニュー", menu = self.menu)
        self.root.config(menu = self.menubar)
        
        # ImagePanel呼び出し
        self.imagePanel = ImagePanel(self.root, self.csvMap['FIRST']['img_name'], self.dirpath)
        
        # AnswerPanel呼び出し
        self.answerPanel = AnswerPanel(self.root, self.csvMap['FIRST']['answer'])
        
        # 文字列Panel設置
        inputTitle = tkinter.StringVar()
        inputTitle.set("キーワード")
        inputTitleFont = font.Font(family = 'Times', size = 10)
        inputTitleLabel = tkinter.Label(self.root, textvariable = inputTitle, font = inputTitleFont)
        inputTitleLabel.pack(fill = 'x', padx = 10, side = 'left')
        
        # InputPanel呼び出し
        self.inputPanel = InputPanel(self.root)
        
        # SubmitPanel呼び出し
        self.submitPanel = SubmitPanel(self.root, self.inputPanel, self.answerPanel, self.imagePanel, self.csvMap, self.dirpath)
    
    def run(self):
        self.root.mainloop()
    
    def exit(self):
        sys.exit(0)
    