#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "HW_API.h"
#include "HW_RangeMask.h"
#include "../include/log.h"
typedef unsigned char BYTE;
typedef unsigned short WORD;
typedef unsigned long DWORD;

unsigned long g_abCsRam[160 * 1024 / 4];
unsigned long g_abEnRam[600 * 1024 / 4];
DWORD adw[128];
short g_anPoint2_1[] = { //我们
		312, 102, 311, 104, 309, 107, 306, 113, 300, 122, 296, 128, -1, 0, 272,
				164, 275, 162, 277, 160, 281, 158, 285, 155, 306, 144, 311, 142,
				319, 138, 326, 135, 330, 134, 332, 134, -1, 0, 299, 142, 299,
				146, 298, 151, 297, 155, 297, 160, 293, 182, 292, 185, 291, 190,
				290, 192, 288, 194, 285, 193, 282, 192, 280, 191, 281, 189, 282,
				187, 287, 182, 296, 173, 303, 165, -1, 0, 318, 120, 318, 125,
				319, 131, 320, 139, 322, 146, 333, 181, 336, 185, 341, 190, 344,
				192, 346, 192, 347, 190, -1, 0, 338, 152, 336, 157, 334, 162,
				331, 166, 329, 169, 321, 182, -1, 0, 331, 110, 333, 112, 335,
				114, 337, 116, 339, 118, 346, 127, -1, 0, 383, 117, 381, 122,
				379, 127, 376, 132, 374, 136, 365, 156, 364, 158, -1, 0, 378,
				148, 378, 150, 378, 154, 379, 157, 379, 161, 378, 178, 378, 180,
				377, 182, 378, 180, -1, 0, 392, 145, 392, 148, 393, 151, 393,
				154, 393, 156, 393, 167, 395, 169, 394, 167, -1, 0, 408, 111,
				408, 114, 409, 116, 410, 118, 413, 129, 413, 132, 413, 135, -1,
				0, 414, 143, 416, 142, 419, 141, 421, 141, 424, 140, 435, 141,
				436, 143, 437, 149, 437, 158, 435, 168, 430, 178, 426, 186, 424,
				189, 422, 190, 418, 186, 416, 183, -1, 0, -1, -1, };

char* RecogCS(short *pnPoint)
{
	char acRst[1024];
	int iRst, i;
	iRst = HWRC_Recognize((unsigned long*) adw, pnPoint);
	LOGV("HWRC_Recognize() %d", iRst);
	if (iRst == HWERR_SUCCESS) {
		iRst = HWRC_GetResult((unsigned long*) adw, 10, acRst);
		LOGV("HWRC_GetResult() %d", iRst);
	}
	for (i = 0; i < iRst; ++i) {
//			printf("%x,",*(WORD*)(acRst+(2*i)));
		LOGV("%x," + *(WORD*)(acRst+(2*i)));
	}
	return acRst;
}

unsigned char * ReadAFile(char *strName, int *piLen) {
	FILE *pF;
	unsigned char *pch;

	*piLen = 0;
	pF = fopen(strName, "rb");
	if (pF == NULL)
		return NULL;
	fseek(pF, 0, SEEK_END);
	*piLen = ftell(pF);
	pch = (unsigned char*) malloc(*piLen);
	fseek(pF, 0, SEEK_SET);
	fread(pch, 1, *piLen, pF);
	fclose(pF);

	return pch;
}

char* TestCSAPI() {
	int iRst;
	char acRst[1024];
	int n;
	int i = 0;
	BYTE* pbDic = NULL;

	memset(adw, 0, 128 * sizeof(DWORD));
	LOGV("TestCSAPI() ");
	iRst = HWRC_SetWorkSpace((unsigned long *) adw, (char *) g_abCsRam,
			SENTENCE_REC_RAM_SIZE);
	LOGV("HWRC_SetWorkSpace() %d", iRst);
	pbDic = ReadAFile("/mnt/sdcard/com_hwrc_18030.bin", &n);
	iRst = HWRC_SetRecogDic((unsigned long*) adw, (const unsigned char*) pbDic);

	iRst = HWRC_SetRecogMode((unsigned long*) adw, HWRC_CHS_SENTENCE);
	LOGV("HWRC_SetRecogMode() %d", iRst);
	iRst = HWRC_SetRecogRange((unsigned long*) adw, ALC_GB18030);
	LOGV("HWRC_SetRecogRange() %d", iRst);
	//只打开识别汉字的范围，在输入短句的时候不会输出标点等，在书写一个字符的时候，可能会输出标点、数字、字母等

	//打开字、数的范围，在输入短句的时候会输出汉字、数字等，在书写一个字符的时候，可能会输出标点、数字、字母等
	//iRst = HWRC_SetRecogRange( (unsigned long*)adw, ALC_GB18030|ALC_NUMERIC);

	//打开字、数、英的范围，在输入短句的时候会输出汉字、数字、英文字母等，在书写一个字符的时候，可能会输出标点、数字、字母等
	//iRst = HWRC_SetRecogRange( (unsigned long*)adw, ALC_GB18030|ALC_NUMERIC|ALC_ALPHA);

	//打开字、数、英、标点的范围，在输入短句的时候会输出汉字、数字、英文字母、标点等，在书写一个字符的时候，可能会输出标点、数字、字母等
	//iRst = HWRC_SetRecogRange( (unsigned long*)adw, ALC_GB18030|ALC_NUMERIC|ALC_ALPHA|ALC_PUN_SYM);
	return RecogCS(g_anPoint2_1);
}

int getCharLen(char* p)
{
	int i = 0;
	for(i=0;i<1024;i++)
	{
		LOGV("getCharLen:%d %d",i,p[i]);
		if(p[i] == 0 && p[i++] == 0)
		{
			return i+1;
		}
	}
}
