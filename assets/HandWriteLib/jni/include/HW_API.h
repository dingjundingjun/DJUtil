/*****************************************************************\
*  基本接口三个：
*   HWRC_SetWorkSpace 加载识别核心工作空间，并填充默认识别配置项。
*  
*   HWRC_Recognize    识别
*  
*   HWRC_GetResult    取识别结果
\*****************************************************************/
//#define WLJ_DEBUG1
#define HANWANG_INTEGRATED_DICT              0//0 //字典集成。 0 字典分离

#define MAX_POINT_NUM           (2048)       //最多输入点数： 2048
#define MIN_REC_RAM_SIZE        (36*1024)    //最小空间，用于单字识别
#define MAX_CANDATION            (10)

#define SINGLE_REC_RAM_SIZE     (36 * 1024 ) //单字识别需要的空间大小
#define SENTENCE_REC_RAM_SIZE   (160*1024)   //短句识别需要的空间大小
#define ENG_REC_RAM_SIZE        (600*1024)  //英文识别需要的识别空间大小
//返回值说明
#define HWERR_SUCCESS                   0		// 成功
#define HWERR_INVALID_PARAMETER         (-1)	// 参数错误
#define HWERR_NOT_ENOUGH_MEMORY         (-2)	// 内存不足
#define HWERR_INVALID_DATA              (-3)	// 无效的字典数据
#define HWERR_INVALID_LANGUAGE			(-5)	// 语言不支持
#define HWERR_NOVALID_RESULT            (-20)   // 没有有效候选
#define HWERR_INVALID_REC_HANDLE        (-21)   // 输入的Handle无效.
#define HWERR_POINTER_NOT_4BYTES_ALGN   (-22)   // 输入的指针没有四字节对齐
#define HWERR_INVALID_RANGE             (-23)   // 识别范围无效
#define HWERR_INVALID_MODE              (-24)   // 识别模式错误


//识别模式
#define HWRC_CHS_SINGLE     1   //中英文单字符识别
#define HWRC_CHS_SENTENCE   2   //中文短句识别
#define HWRC_LATIN_WORD     3   //西文单词识别


//如果在书写框格子里书写，格子的结构定义
#ifndef THWFRAME
#define THWFRAME
typedef struct tagTHWFrame{
	short left;		// 书写框左上角 X 坐标
	short top;		// 书写框左上角 Y 坐标
	short width;	// 书写框宽度
	short height;	// 书写框高度
} THWFrame;
#endif

#define  HWAPI  

#ifdef __cplusplus
extern "C"
{
#endif

//=======================================================================
//加载识别核心工作空间
//pHandle   [in]识别句柄 128 个DWORD
//pcRam     [in] 识别空间，四字节起始。根据单字或多字识别要求，申请相应空间大小
//iRamSize  [in] 识别空间大小
//return:   
//  HWERR_SUCCESS
//  HWERR_INVALID_REC_HANDLE
//  HWERR_INVALID_PARAMETER
//  HWERR_NOT_ENOUGH_MEMORY
 int HWRC_SetWorkSpace( unsigned long *pHandle, char *pcRam, long lRamSize );

//===============================================================================
//识别。默认是单字识别。 HWRC_SetRecogMode 可修改为中文短句或英文单词。
//dwHandle  [in]识别句柄 128 个DWORD
//pnPoint   [in]笔迹。（-1，0)每个笔画结束，(-1,-1)整个笔迹结束。
//return:   
//  见返回值说明
 int HWRC_Recognize( unsigned long * pHandle, short *pnPoint );
                    
//===============================================================================
//取得识别候选（单字识别或短句、单词)
//iMaxCandNum  [in]最大候选个数
//pResult      [out] 默认UCS2编码。每个候选以0结尾。所有候选结束后，有0结尾。
//              
//             如：
//             中文单字识别三个候选“一二三”
//                           0x4E00,0x0000,  0x4E8C, 0x0000,   0x4E09,0x0000
//                           0x0000 所有候选结束
//             中文多字识别下三个候选： 我的，我，一二，
//                          0x6211,0x7684,0x0000, 
//                          0x6211,0x0000,  
//                          0x4E00,0x4E8C, 0x0000
//                          0x0000 所有候选结束
//             英文写一个单词，如果返回两个候选串 good god
//                          0x0067 0x006F 0x006F 0x0064 0x0000 
//                          0x0067 0x006F 0x0064 0x0000
//                          0x0000 //全部候选结束
//
//             英文书写了两个单词，每个单词有两个候选 good god， Yes yea
//             本函数返回第一个单词的候选，
//                      0x0067 0x006F 0x006F 0x0064 0x0000    good
//                      0x0067 0x006F 0x0064 0x0000           god
//                      0x0000
//                      调用HWRC_GetNextBlockResult得到下个单词的候选。如果为0，则全部候选输出完毕。没有下一个单词了。
//                      0x0059 0x0065 0x0073 0x0000           yes
//                      0x0059 0x0065 0x0061 0x0000           yea
//                      0x0000
//
//return: 如果pResult != NULL ,
//            返回>=0:   实际候选个数. 0候选个数为 0.
//            HWERR_INVALID_REC_HANDLE 识别句柄无效
//            HWERR_INVALID_PARAMETER
//            HWERR_INVALID_RESULT 没有有效的候选
//
//        如果pResult ==NULL
//            返回>=0:  需要的存储空间字节个数。
//            HWERR_INVALID_REC_HANDLE 识别句柄无效
//            HWERR_INVALID_PARAMETER
//            HWERR_INVALID_RESULT 没有有效的候选
//
 int HWRC_GetResult( unsigned long *pHandle, int iMaxCandNum, char *pResult );


/****************************************************************************\
*  扩展接口11个
*      HWRC_SetRecogMode 设置识别方式： 中文单字、短句、英文词
*      HWRC_GetRecogMode 
*      
*      HWRC_SetRecogDic  字典分离情况下设置识别字典
*      HWRC_GetRecogDic  
*      
*      HWRC_SetRecogLanguage 设置识别语言
*      HWRC_GetRecogLanguage 
*      
*      HWRC_SetRecogRange    设置识别范围
*      HWRC_GetRecogRange    设置识别范围
*      
*      HWRC_SetInputBox      设置手写框
*      HWRC_GetInputBox      设置手写框
*      
*      HWRC_RecogIncreasing  中文多字下抬笔即识。
*      
\****************************************************************************/

//设置识别方式：单字识别\多字识别\英文识别。
//dwHandle  [in]识别句柄 128 个DWORD
//iType     [in] 识别方式，
//                HWRC_CHS_SINGLE, 单字识别
//                HWRC_CHS_SENTENCE，短句识别
//                HWRC_LATIN_WORD 单词识别
//return:   见返回值说明
//      HWERR_SUCCESS
//      HWERR_INVALID_MODE 识别模式错误
//返回值, HWERR_INVALID_REC_HANDLE hHandle错误。
 int HWRC_SetRecogMode( unsigned long *pHandle, int iType );

//======================================================
//取识别方式。
//return：
//                HWRC_CHS_SINGLE, 单字识别
//                HWRC_CHS_SENTENCE，短句识别
//                HWRC_LATIN_WORD 单词识别
//
//返回值, HWERR_INVALID_REC_HANDLE hHandle错误。
 int HWRC_GetRecogMode( unsigned long *pHandle);

//======================================================
//外部识别字典非集成情况下，设置识别字典。
//引擎内部来解析字典。
//pbDic [in]识别字典。必须是从四字节对齐的地址开始存放的。
//return:   见返回值说明
//      HWERR_SUCCESS
//      HWERR_INVALID_REC_HANDLE
//      HWERR_INVALID_PARAMETER
//      HWERR_POINTER_NOT_4BYTES_ALGN
 int HWRC_SetRecogDic( unsigned long *pHandle, const unsigned char *pbDic );

//======================================================
//取得设置的字典指针。
//return:   指向字典
//      返回值 NULL, pHandle错误。
 const unsigned char *HWRC_GetRecogDic( unsigned long *pHandle );

//设置识别语言，如不调用此函数，按照字典中默认语言识别。
//return:   见返回值说明
//int HWRC_SetRecogLanguage( unsigned long *pHandle, unsigned long dwLanguage );

//取识别语言
//return: 识别语言
//unsigned long HWRC_GetRecogLanguage( unsigned long *pHandle );

//======================================================
//设置识别范围. 如不调用此函数，按照字典中的默认设置范围识别。
//中文默认识别范围：简体一二级， 英文默认识别模式常用标点符号+英文单词。
//
//如输入 dwRange == 0, 则设置为默认识别范围。
//设置范围要注意识别的模式，如果设置的范围不在识别模式的范围中，则设置失败。
//如:识别模式如果设置了HWRC_LATIN_WORD，则若设置识别范围 ALC_PUNC_RARE ，则设置失败。
//return:  
//      HWERR_SUCCESS
//      HWERR_INVALID_REC_HANDLE
//      HWERR_INVALID_RANGE
 int HWRC_SetRecogRange( unsigned long *pHandle, unsigned long dwRange );

//======================================================
//取当前识别范围
//return: 识别范围
// HWERR_INVALID_REC_HANDLE;
 unsigned long HWRC_GetRecogRange( unsigned long *pHandle);

//======================================================
//设置输入框
//return:   见返回值说明
//  HWERR_SUCCESS
//  HWERR_INVALID_REC_HANDLE
//  HWERR_INVALID_PARAMETER
 int HWRC_SetInputBox( unsigned long *pHandle, const THWFrame *pFrame );

//======================================================
//取得设置的框大小
//return:   
//  HWERR_SUCCESS
//  HWERR_INVALID_REC_HANDLE
//  HWERR_INVALID_PARAMETER
 int HWRC_GetInputBox( unsigned long *pHandle, THWFrame *pFrame );

//抬笔即识方式:
//中文多字识别方式下，每写一笔调用一次，笔迹逐渐增长。每笔画结束标志(-1,0)。如果全部笔画结束，最后为（-1,-1)
//pnPoint   [in] 笔迹点
//iPointNum [in] 笔迹长度:包含结束标志的笔记点个数。
//return:   见返回值说明
//int HWRC_RecogInc( unsigned long * pHandle, short *pnPoint, int iPointNum );



#ifdef __cplusplus
}
#endif
