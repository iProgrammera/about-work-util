-- Create table//此表是用户表，内容自行维护
create table SIPF_USER
(
  USER_ID    VARCHAR2(5) not null,
  NAME       VARCHAR2(10),
  LOGIN      VARCHAR2(20),
  DEPARTMENT VARCHAR2(20)
)
tablespace XINFANG;



-- Create table
create table SIPF_SALARY
(
  USER_ID                  VARCHAR2(5),
  SALARY_ID                VARCHAR2(10) not null,
  SALARY_DATE              DATE,
  SALARY_JIBENGONGZI       NUMBER(10,2),
  SALARY_GANGWEIJINTIE     NUMBER(10,2),
  SALARY_GUOJIEFEI         NUMBER(10,2),
  SALARY_FANGSHUJIANGWEN   NUMBER(10,2),
  SALARY_GONGZIBUFA        NUMBER(10,2),
  SALARY_TOTAL             NUMBER(10,2),
  SALARY_GONGJIJINCHAOXIAN NUMBER(10,2),
  SALARY_CCB               NUMBER(10,2),
  SALARY_BCM               NUMBER(10,2),
  SALARY_BEIZHU            VARCHAR2(100),
  DED_KAOQIN               NUMBER(10,2),
  DED_YANGLAO              NUMBER(10,2),
  DED_YILIAO               NUMBER(10,2),
  DED_SHIYE                NUMBER(10,2),
  DED_GONGJIJIN            NUMBER(10,2),
  DED_NIANJIN              NUMBER(10,2),
  DED_DANGFEI              NUMBER(10,2),
  DED_GERENSUODESHUI       NUMBER(10,2),
  DED_TOTAL                NUMBER(10,2),
  DED_YANGLAOCHAE          NUMBER(10,2),
  DED_YILIAOCHAE           NUMBER(10,2),
  DED_SHIYECHAE            NUMBER(10,2),
  DED_GONGJIJINCHAE        NUMBER(10,2),
  DED_NIANJINTIAOZHENG     NUMBER(10,2),
  TAX_SHUIQIANKOUCHU       NUMBER(10,2),
  TAX_YINGNASHUIE          NUMBER(10,2),
  TAX_SHUILV               NUMBER(10,2),
  TAX_SUSUAN               NUMBER(10,2),
  TAX_TOTAL                NUMBER(10,2)
);

-- Create sequence 
create sequence SEQ_SIPF_SALARY
minvalue 1
maxvalue 9999999999999999999999999999
start with 1
increment by 1
cache 20;