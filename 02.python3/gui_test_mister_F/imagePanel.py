# -*- coding: utf-8 -*-
from tkinter import font
import tkinter

"""
    imagePanel.py
    
    サブクラス ImagePanel。
    画像パネル作成、各Action処理実装クラス。
    
    _2019/06/25 CREATE at per6_jp
    
"""

class ImagePanel:
    image       = None
    imageFrm    = None
    imageCanvas = None
    dirpath     = None
    
    def __init__(self, root, image, dirpath):
        self.dirpath = dirpath
        
        # 画像を表示してみる
        self.setImage(self.dirpath + "/img/" + image + ".png")
        
        # パネル作成
        self.imageFrm = tkinter.Frame(root, width = 280, height = 160)
        self.imageFrm.pack(side = tkinter.TOP, pady = 10)
        
        self.imageCanvas = tkinter.Canvas(self.imageFrm, width = 280, height = 160)
        self.imageCanvas.place(x = 0, y = 0)
        self.imageCanvas.create_image(0, 0, image = self.image, anchor = tkinter.NW)
        
    def setImage(self, imgPath):
        self.image = tkinter.PhotoImage(file = imgPath)
        
    def showImage(self):
        self.imageCanvas.create_image(0, 0, image = self.image, anchor = tkinter.NW)