安装后第一次打开会弹出使用情况访问权限
 ![image](https://user-images.githubusercontent.com/74522766/233319173-7804b653-e19d-4985-8c2c-6217e5f834ea.png)

第一部分：统计 获取手机各软件使用时间，在手机/data/system/usagestats（或者是/user_id / TimeStamp）中保存着关于系统记录的每一个activity的使用记录。每一个文件记录一天的数据，一般为早上8点开始。使用UsageStatsService获取文件,要在manifest中申明权限：PACKAGE_USAGE_STATS。饼图展示排行前五的软件，列表按时间顺序展示
 ![image](https://user-images.githubusercontent.com/74522766/233318388-652f5866-782b-417a-9c97-a2ccb85147fc.png)

第二部分：图标 折线图显示近一周手机使用总时间（以最大值作为y轴最高点）
 ![image](https://user-images.githubusercontent.com/74522766/233313994-3434fe12-495c-4bef-af3a-4ebf23c6bb20.png)

第三部分：锁机 选择应用，设置解锁时间，软件被成功上锁
 ![image](https://user-images.githubusercontent.com/74522766/233316353-697bcc56-7e5f-47f7-a4a9-3d6b4d46c888.png)
 ![image](https://user-images.githubusercontent.com/74522766/233318898-2edb1648-e76d-490a-ad66-931c99044615.png)

