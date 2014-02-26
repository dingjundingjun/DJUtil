//-----------------------------------------------------------------
//              简体中文识别范围设置：（单字识别，短句识别)
#define ALC_SC_COMMON		0x00000001		/* 简体一级字 */
#define ALC_SC_RARE			0x00000002		/* 简体二级字 */
#define ALC_SC_GBK34		0x00000004   //0x00000004		/* GBK34 */
#define ALC_GBK         0x00000007      /* GBK */  //行识别中文识别范围
#define ALC_SC_GB18030EX     0x00000008      /* GB18030Add */

#define ALC_GB18030          0x0000000F      /* GB18030 */

#define ALC_SC_RADICAL	0x00200000  //0x00200000		/* 偏旁部首，只适用于简体版 */

//----------------------------------------------------------------------------------------------------------------------------------
#define ALC_NUMERIC			0x00000100  		/* 数字 0-9 ) */

#define ALC_UCALPHA			0x00000200  		/* 大写字母( A-Z ) */
#define ALC_LCALPHA			0x00000400  		/* 小写字母 ( a-z ) */
#define ALC_ALPHA       0x00000600      /*All Latin Letters*/


#define ALC_PUNC_COMMON		0x00000800 
#define ALC_PUNC_RARE			0x00001000 
#define ALC_SYM_COMMON		0x00002000
#define ALC_SYM_RARE			0x00004000
#define ALC_PUN_SYM				0x00007800 /*所有标点符号*/

#define	ALC_GESTURE			0x00008000

#define ALC_CS_CURSIVE      0x00010000      /* 行草字 */
#define ALC_ZHUYIN          0x00020000
#define ALC_HIRAGANA        0x00040000      
#define ALC_KATAKANA        0x00080000


//默认打开文字宏
#define ALC_CHS_GB2312      (ALC_SC_COMMON|ALC_SC_RARE|ALC_NUMERIC|ALC_UCALPHA|ALC_LCALPHA|ALC_PUNC_COMMON|\
  ALC_PUNC_RARE|ALC_SYM_COMMON|ALC_SYM_RARE|ALC_GESTURE|ALC_CS_CURSIVE)//0x0101FFFF//识别范围：GB2312 + 文字宏

#define ALC_CHS_GBK          (ALC_CHS_GB2312|ALC_GBK)
#define ALC_CHS_GB18030   (ALC_CHS_GB2312|ALC_GB18030)

#define ALC_VALID_SC_CHS      (ALC_CHS_GB2312|ALC_SC_RADICAL|ALC_GB18030|ALC_HIRAGANA|ALC_KATAKANA|ALC_GESTURE) /* 有效识别范围，默认开启写繁体得简体 */

#define ALC_VALID_TC_CHS       0x000fffff      /* 有效繁体识别范围 */

#define ALC_VALID_CHS              0xffffffff


//************************************************************************************************************
//  英文识别范围设置 (英文单词识别 ）
#define ENG_COM_PUNC_SYMBOL 	0x00000001  //常用标点符号
#define ENG_EXT_PUNC_SYMBOL     0x00000002  //扩展标点符号
#define ENG_GESTURE				0x00000004	// 控制手势（空格 制表符 回删 回车）
#define ENG_MULTIWORD           0x00000008  // 多单词识别.不选则为一个单词识别。

#define ALC_VALID_LAT           0x0000000F
