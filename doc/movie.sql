/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50703
Source Host           : localhost:3306
Source Database       : movie

Target Server Type    : MYSQL
Target Server Version : 50703
File Encoding         : 65001

Date: 2015-08-03 09:45:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `accounName` varchar(64) NOT NULL COMMENT '帐户名',
  `trueName` varchar(64) DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `sex` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0:男，1:女',
  `avatar` varchar(128) DEFAULT NULL COMMENT '头像',
  `signature` varchar(128) DEFAULT NULL COMMENT '个性签名',
  `address` varchar(128) DEFAULT NULL COMMENT '常住地',
  `balance` int(10) NOT NULL DEFAULT '0' COMMENT '余额，单位为分，避免小数四舍五入',
  `steps` tinyint(2) DEFAULT '0' COMMENT '步骤，默认0：新注册，1：已完善',
  `role` tinyint(2) NOT NULL DEFAULT '0' COMMENT '角色默认为0：普通用户，1：vip，2：admin',
  `createAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `isDeleted` int(2) DEFAULT '0' COMMENT '是否已删除：0否，1是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('9', 'admin', 'admin', '0e034c5747c45ee2247d3682c2852adb', '417371811@qq.com', '18810733224', '0', null, '在', '北京', '0', '0', '2', '2015-06-29 17:32:39', '0');


SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_movie`
-- ----------------------------
DROP TABLE IF EXISTS `t_movie`;
CREATE TABLE `t_movie` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '电影名字',
  `address` varchar(1024) DEFAULT NULL COMMENT '下载地址',
  `content` text COMMENT '推荐内容',
  `year` varchar(64) DEFAULT NULL COMMENT '上映时间',
  `actors` varchar(255) DEFAULT NULL COMMENT '演员',
  `downNum` int(11) DEFAULT '0' COMMENT '下载次数',
  `country` varchar(64) DEFAULT NULL COMMENT '国家地区',
  `img` varchar(1024) DEFAULT NULL COMMENT '详情图片地址',
  `isrecommend` int(2) DEFAULT '0' COMMENT '是否是首页推荐电影0否，1是',
  `recUrl` varchar(1024) DEFAULT NULL COMMENT '推荐URL',
  `rating` int(2) DEFAULT '5' COMMENT '推荐指数：0~5',
  `smallUrl` varchar(1024) DEFAULT NULL COMMENT '小图地址',
  `createTime` datetime DEFAULT NULL COMMENT '添加时间',
  `isDeleted` int(2) DEFAULT '0' COMMENT '是否删除0：否，1是',
  `category` varchar(64) DEFAULT NULL COMMENT '导演',
  `type` int(2) DEFAULT '1' COMMENT '类别1:推荐,2搞笑，3恐怖，4记录，5专题，6动作',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_movie
-- ----------------------------
INSERT INTO `t_movie` VALUES ('1', '阿甘正传', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435586451035.torrent', '《阿甘正传》是由罗伯特·泽米吉斯执导的电影，由汤姆·汉克斯，罗宾·怀特等人主演，于1994年7月6日在美国上映。电影改编自美国作家温斯顿·格卢姆于1986年出版的同名小说，描绘了先天智障的小镇男孩福瑞斯特·甘自强不息，最终“傻人有傻福”地得到上天眷顾，在多个领域创造奇迹的励志故事。电影上映后，于1995年获得奥斯卡最佳影片奖、最佳男主角奖、最佳导演奖等6项大奖。2014年9月5日，在该片上映20周年之际，《阿甘正传》IMAX版本开始在全美上映。', '1994年', '汤姆·汉克斯，罗宾·怀特等', '11', '美国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435586427469.jpg', '1', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435586410030.jpg', '9', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435586433873.png', '2015-06-29 22:03:42', '0', '罗伯特·泽米吉斯', '1');
INSERT INTO `t_movie` VALUES ('2', '百万宝贝', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587034095.torrent', '《百万宝贝》（Million Dollar Baby）是一部励志剧情电影。影片由克林特·伊斯特伍德执导，克林特·伊斯特伍德、希拉里·斯万克、摩根·弗里曼等主演。影片讲述了一位有名的拳击教练法兰基因为太过于投身与拳击事业而陷入了长期的自我封闭和压抑，一位学徒麦琪坚毅的决心软化了法兰基并成出色的女拳击手。影片于2004年在美国上映', '2004年12月15日', '克林特·伊斯特伍德、希拉里·斯万克、摩根·弗里曼等', '23', '美国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587018360.jpg', '0', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587007569.jpg', '8', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587026845.jpg', '2015-06-29 22:10:50', '0', '克林特·伊斯特伍德', '1');
INSERT INTO `t_movie` VALUES ('3', '变脸', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587246999.torrent', '《变脸》是一部1998年派拉蒙影业公司出品的动作剧情片，由吴宇森执导，尼古拉斯·凯奇、约翰·特拉沃塔、琼·艾伦、亚历桑德罗·尼沃拉和吉娜·格申等联袂出演。影片于1997年6月27日在美国上映。电影讲述FBI探员亚瑟追捕恐怖杀手凯斯特长达8年之久，凯斯曾杀害他的儿子，两人因此结下了不共戴天之仇。一次，为了调查致命炸弹的放置地点，亚瑟自愿取下自己的脸皮，换上昏迷中凯斯特的脸，混入监狱与其匪党搭上线，以套出炸弹的放置地点。但是清醒后的凯斯也换上了亚瑟的脸，摇身一变为FBI探员，使亚瑟的家庭与 ..', '1997年6月27日', '尼古拉斯·凯奇、约翰·特拉沃塔、琼·艾伦、亚历桑德罗·尼沃拉和吉娜·格申等', '23', '	美国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587220828.jpg', '0', null, '9', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587232872.png', '2015-06-29 22:14:39', '0', '吴宇森', '1');
INSERT INTO `t_movie` VALUES ('4', '兵临城下', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587450427.torrent', '《兵临城下》是由让·雅克·阿诺导演执导，裘德·洛、艾德·哈里斯主演的电影，2001年在中国大陆上映。影片改编自作家威廉·克雷格1973年创作的同名纪实小说。该片讲述第二次世界大战时，苏联红军传奇狙击手瓦西里·柴瑟夫与德军顶尖的神枪手康尼少校，在斯大林格勒战役中的一场生死之战。', '2001年7月21日', '裘德·洛、艾德·哈里斯', '22', '美国，德国，英国，爱尔兰', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587416523.png', '0', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587416523.png', '8', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587442560.jpg', '2015-06-29 22:17:41', '0', '让·雅克·阿诺', '1');
INSERT INTO `t_movie` VALUES ('5', '持续惊恐', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587603068.torrent', '《持续惊恐》是一部枪战警匪类型的美国电影，讲述了乔依从事专为黑社会“洗枪”的行当，不守规矩的是，他常常把那些正被警方严密追查的枪支藏在自己的密室里，而不是依行规将他们彻底销毁。一天，乔依这一举动被10岁的儿子和其小伙伴奥利格目睹，心术不正的奥利格随后偷偷从中取出一支0.38手枪，并当天用它击伤了虐待自己的继父......', '2006年1月6日', '保罗·沃克，卡梅隆·布莱特等', '33', ' 美国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587556712.jpg', '0', null, '9', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587585216.png', '2015-06-29 22:20:21', '0', 'Wayne Kramer', '1');
INSERT INTO `t_movie` VALUES ('6', '当幸福来敲门', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587826516.torrent', '《当幸福来敲门》是由加布里尔·穆奇诺执导，威尔·史密斯等主演的美国电影。影片取材真实故事，主角是美国黑人投资专家Chris Gardner。影片讲述了一位濒临破产、老婆离家的落魄业务员，如何刻苦耐劳的善尽单亲责任，奋发向上成为股市交易员，最后成为知名的金融投资家的励志故事。影片获得2007年奥斯卡金像奖最佳男主角的提名。', ' 2006年12月15日', '威尔·史密斯等', '52', '美国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587763452.jpg', '1', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587699578.jpg', '10', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435587773703.png', '2015-06-29 22:23:50', '0', '加布里尔·穆奇诺', '1');
INSERT INTO `t_movie` VALUES ('7', '风月俏佳人', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588066798.torrent', '《风月俏佳人》是由滚石公司于1989年出品的爱情喜剧片，影片由盖瑞·马歇尔执导，J.F. Lawton编剧，理查·基尔、朱莉娅·罗伯茨、拉尔夫·贝拉米、杰森·亚历山大等主演。影片改编自同名小说，主要讲述了是关于一个洛杉矶妓女维维安（朱莉亚·罗伯茨饰）和一个身家百万的企业巨头爱德华·刘易斯（理查·基尔 饰）的错综复杂以及浪漫的爱情故事。影片于1990年3月23日在美国上映，全球累计票房4亿6000万美元，斩获包括第63届奥斯卡最佳女主角提名在内的等多项电影奖项目。', '1990年3月23日', '理查·基尔、朱莉娅·罗伯茨、拉尔夫·贝拉米等', '12', '美国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588045742.jpg', '0', null, '7', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588057309.png', '2015-06-29 22:28:08', '0', '盖瑞·马歇尔', '1');
INSERT INTO `t_movie` VALUES ('8', '光辉岁月', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588233294.torrent', '黑人美式足球教练─布恩教练〔丹泽尔·华盛顿饰演〕，在种族融合的政策下，来到一所白人高中担任足球教练，同一时间，黑人学生也由另一所学校被迫转学到此。一向以白人为主的维吉尼亚小镇，顿时之间来了许多陌生黑人面孔，对高中学生而言以及他们的家长造成不小的震撼。布教练从一开始遭到所有人的排斥到凭著自己的信念以及白人副教练的配合之下，成功地将这一只长败军的黑白混合美式足球队伍推至冠军宝座，同时也成功写下黑白融合的成功案例。', '1999年', '丹泽尔·华盛顿，威尔·帕顿等', '13', '美国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588188969.jpg', '0', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588188969.jpg', '8', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588209587.png', '2015-06-29 22:30:35', '0', 'Boaz Yakin', '1');
INSERT INTO `t_movie` VALUES ('9', '美国黑帮', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588372540.torrent', '《美国黑帮》（American Gangster）由环球影业发行的犯罪影片，该影片由雷德利·斯科特执导，拉塞尔·克罗、丹泽尔·华盛顿领衔主演。该影片改编自《纽约》杂志上的一篇关于70年代黑人毒枭弗兰克-卢卡斯的真实故事，主要讲述了1970年代海洛因毒枭弗兰克·卢卡斯和一名纽约警察里奇·罗伯茨互相斗智斗勇的故事。影片于2007年11月2日全美地区发行。影片2007年上映荣是获奥斯卡最佳影女配角及美国金球奖最佳影片等12想提名。', '2007年10月19日', '罗素·克劳，丹泽尔·华盛顿等', '44', '美国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588335570.jpg', '0', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588335570.jpg', '8', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588347921.png', '2015-06-29 22:33:22', '0', '雷德利·斯科特', '1');
INSERT INTO `t_movie` VALUES ('10', '梦精记2', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588555968.torrent', '圣恩、秀妍和美淑三名高中女生在参加了姐姐的婚礼后浮想联翩，期待自己的白马王子早日到来……刚毕业的英俊体育老师姜奉求为自己面对女性激动时便会放屁的奇异体制苦恼不已，然而这不妨碍他在女校实习时成为了女生的大众情人。圣恩和盛气凌人的校园模特白世美同时属意姜奉求，一场竞争不可避免。胸部平平又毫无和异性交往经验的圣恩处处落于下风，秀妍与美淑两个姐妹帮忙策划了男生兴奋点研究、偷窥男更衣室和匿名录音带表白等一系列夺男行动却始终不见成效，面对白世美的步步紧逼，圣恩决定孤注一掷，直接在夜晚闯入了姜奉求的 ...', '2005年1月13日', '郑栋焕，姜恩菲，全慧彬等', '33', '韩国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588503513.jpg', '1', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588490004.jpg', '7', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588510054.png', '2015-06-29 22:36:10', '0', '丁楚信', '1');
INSERT INTO `t_movie` VALUES ('11', '谜一样的眼睛', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588745922.torrent', '谜一样的眼睛是由胡安·何塞·坎帕内利亚执导，里卡杜·达林、索蕾达·维拉米尔等主演的一部阿根廷爱情电影。', '2009年8月13日', '里卡杜·达林，索蕾达·维拉米尔，帕博罗·拉格，古勒莫·法兰塞拉', '32', '西班牙', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588724748.jpg', '0', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588716418.jpg', '6', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588736812.png', '2015-06-29 22:39:13', '0', '胡安·何塞·坎帕内利亚', '1');
INSERT INTO `t_movie` VALUES ('12', '贫民窟的百万富翁', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588880503.torrent', '影片《贫民窟的百万富翁》由英国导演丹尼·博伊尔所执导，根据印度作家维卡斯·史瓦卢普（Vikas Swarup）的作品《Q&A》所改编的。戴夫·帕特尔、芙蕾达·平托、亚尼·卡普和沙鲁巴·舒克拉等联袂出演。影片于2009年3月26日在中国上映。电影讲述来自贫民窟的印度街头少年贾马勒参加了电视节目《谁想成为百万富翁》，他的目的是要找回失踪的女朋友拉媞卡，因他的女朋友对这个电视节目一向十分热衷。但当他即将获取高额奖金时，却被人揭发有作弊嫌疑。', '2008年8月30日', '戴夫·帕特尔，芙蕾达·平托等', '45', '印度', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588859490.jpg', '1', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588845628.jpg', '10', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588874566.png', '2015-06-29 22:41:24', '0', '丹尼·博伊尔 Loveleen Tandan', '1');
INSERT INTO `t_movie` VALUES ('13', '七宗罪', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435592045556.torrent', '《七宗罪》是一部由大卫·芬奇执导，布拉德·皮特、摩根·弗里曼、格温妮丝·帕特洛、凯文·史派西等人主演的惊悚悬疑片。该片以罪犯约翰·杜制造的连环杀人案件为线索，从警员沙摩塞和米尔斯的视角出发，讲述了“七宗罪”系列谋杀案的故事。1995年该片在美国上映。1996年该片获得了第5届MTV电影奖最佳影片等奖项。', '1995年9月22日', '布拉德·皮特、摩根·弗里曼、格温妮丝·帕特洛、凯文·史派西等', '37', '美国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588959015.jpg', '0', null, '8', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435588967935.png', '2015-06-29 22:42:52', '0', '大卫·芬奇', '1');
INSERT INTO `t_movie` VALUES ('14', '启示录', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435589221703.torrent', '《启示录》是2005年梅尔·吉布森执导美国电影，讲述玛雅文明末期，奢华淫靡之气蔓延，为了祭奠那些刚刚落成的金字塔以及驱散众神的愤怒，玛雅王国派出强悍的军队入侵丛林深处的弱小部落，将战俘作为祭天的牺牲斩杀。 年轻骁勇的战士虎爪和族人过着平静的生活，他有可爱的儿子和美丽的妻子，并且不久将迎来新的生命。但玛雅军队打破了这一切，他的部落遭到袭击，虎爪及时将妻儿藏在深坑之中，自己却不慎被俘。 经过一路坎坷，他和其他战俘来到玛雅城。无数头颅被可耻的刽子手斩落，但即便在这个时刻，虎爪也念念不忘丛 ... ', ' 2006年12月08日', '达莉亚·赫尔南德兹 Dalia Hernandez', '13', '美国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435589198185.jpg', '0', null, '8', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435589205918.png', '2015-06-29 22:47:04', '0', '梅尔·吉布森', '1');
INSERT INTO `t_movie` VALUES ('15', '窃听风暴', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435589340422.torrent', '《窃听风暴》是32岁的年轻导演唐纳斯马克的第一部故事长片，该片讲述了一名东德国安局情报员由忠于职守转而对自己的工作失去热情，继而改变立场，试图保护上级要求他侦察的女对象。该片获2007年第79届奥斯卡金像奖最佳外语片。', '2006年03月23日', '乌尔里希·穆埃，塞巴斯蒂安·考奇，马蒂娜·戈黛特', '31', '德国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435589324217.png', '0', null, '9', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435589334268.png', '2015-06-29 22:49:02', '0', '弗洛里安·亨克尔·冯·多纳斯马', '1');
INSERT INTO `t_movie` VALUES ('16', '忠犬八公的故事', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435589437605.torrent', '影片《忠犬八公的故事》改编自1933年发生在日本的真实故事，由莱塞·霍尔斯道姆执导，理查·基尔、琼·艾伦和萨拉·罗默尔等联袂出演。影片于2009年8月8日在故事的原型故乡日本率先上映。影片讲述一位大学教授收养了一只小秋田犬，取名“八公”。之后的每天，八公早上将教授送到车站，傍晚等待教授一起回家。不幸的是，教授因病辞世，再也没有回到车站，然而八公在之后的9年时间里依然每天按时在车站等待，直到最后死去。', '2009年12月18日', '理查·基尔、琼·艾伦，萨拉·罗默尔等', '11', '美国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435589418057.jpeg', '0', null, '8', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435589431429.png', '2015-06-29 22:50:40', '0', '莱塞·霍尔斯道姆', '1');
INSERT INTO `t_movie` VALUES ('17', '生死豪情', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435589601192.torrent', '《闰年》是环球影业出品的一部爱情电影，由安南德·图克尔执导，艾米·亚当斯、马修·古迪、 亚当·斯科特等联袂出演。影片于2010年1月8日在美国上映。影片讲述了女孩安娜为了嫁给完美男人，决定在2月29日这天向男朋友求婚，而在这个过程中出现了各种意外，邂逅了小旅馆帅老板德克兰，安娜的爱情发生了改变。', '1996年7月12日', '丹泽尔·华盛顿，梅格·瑞安，卢·戴蒙德，菲利', '56', '美国', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435589585511.jpg', '0', null, '8', 'http://7tsxy9.com1.z0.glb.clouddn.com/1435589591774.png', '2015-06-29 22:53:25', '0', '爱德华·兹威克', '2');
