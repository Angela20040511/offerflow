DELETE FROM interview_record;
DELETE FROM favorite_job;
DELETE FROM application_record;
DELETE FROM resume;
DELETE FROM guide_doc;
DELETE FROM job;
DELETE FROM region_city;
DELETE FROM region_province;
DELETE FROM job_category;
DELETE FROM subsidiary_company;
DELETE FROM sys_user;

INSERT INTO sys_user (id, username, password, role, email, phone, avatar, status, create_time, update_time)
VALUES
  (1, 'student01', '123456', 'APPLICANT', 'student01@example.com', '13800000000', '/static/avatar/default.png', 1, NOW(), NOW()),
  (2, 'student02', '123456', 'APPLICANT', 'student02@example.com', '13800000001', '/static/avatar/default.png', 1, NOW(), NOW()),
  (3, 'hradmin', '123456', 'HR', 'hradmin@example.com', '13900000000', '/static/avatar/default.png', 1, NOW(), NOW());

INSERT INTO subsidiary_company (id, group_name, subsidiary_name, short_name, business_line, intro, status, sort_num, create_time, update_time)
VALUES
  (1, '用友集团', '用友网络科技股份有限公司', '用友网络', '智能云平台', '承接集团级招聘平台、协同产品和企业软件研发能力。', 1, 1, NOW(), NOW()),
  (2, '用友集团', '用友广信', '用友广信', '政企数智服务', '面向政企客户提供数字化咨询、交付和运营服务。', 1, 2, NOW(), NOW()),
  (3, '用友集团', '用友金融信息技术股份有限公司', '用友金融', '金融科技', '聚焦金融科技平台、风控产品和数据服务建设。', 1, 3, NOW(), NOW()),
  (4, '用友集团', '用友汽车信息科技（上海）股份有限公司', '用友汽车', '汽车数智化', '围绕汽车产业链提供业务协同与数据中台能力。', 1, 4, NOW(), NOW()),
  (5, '用友集团', '用友能源科技有限公司', '用友能源', '能源数字化', '服务能源行业数字化经营、产品和增长运营。', 1, 5, NOW(), NOW()),
  (6, '用友集团', '用友审计软件有限公司', '用友审计', '审计与风控', '面向审计和风控场景提供软件、数据和服务支持。', 1, 6, NOW(), NOW());

INSERT INTO job_category (id, category_name, parent_id, sort_num, status, create_time, update_time)
VALUES
  (1, '技术研发', NULL, 1, 1, NOW(), NOW()),
  (2, '产品', NULL, 2, 1, NOW(), NOW()),
  (3, '数据', NULL, 3, 1, NOW(), NOW()),
  (4, '设计', NULL, 4, 1, NOW(), NOW()),
  (5, '市场运营', NULL, 5, 1, NOW(), NOW());

INSERT INTO region_province (code, name, sort_num)
VALUES
  ('110000', '北京市', 1),
  ('310000', '上海市', 2),
  ('320000', '江苏省', 3),
  ('330000', '浙江省', 4),
  ('420000', '湖北省', 5),
  ('440000', '广东省', 6),
  ('510000', '四川省', 7);

INSERT INTO region_city (code, province_code, name, sort_num)
VALUES
  ('110100', '110000', '北京市', 1),
  ('310100', '310000', '上海市', 1),
  ('320100', '320000', '南京市', 1),
  ('330100', '330000', '杭州市', 1),
  ('420100', '420000', '武汉市', 1),
  ('440100', '440000', '广州市', 1),
  ('440300', '440000', '深圳市', 2),
  ('510100', '510000', '成都市', 1);

INSERT INTO job (id, subsidiary_id, job_category_id, title, company, city, category, type, business_line, province_code, city_code, work_mode, duration_type, internship_length, required_skills_json, tags, salary_min, salary_max, description, requirement, status, publish_time, create_time, update_time)
VALUES
  (101, 1, 1, '前端开发实习生', '用友网络科技股份有限公司', '北京市', '技术研发', 'LONG_TERM', '智能云平台', '110000', '110100', 'ONSITE', 'LONG_TERM', '3个月及以上', '["Vue 3","TypeScript","Element Plus","工程化"]', '["Vue 3","TypeScript","后台系统","组件化"]', 180, 260, '参与集团招聘门户和内部协同平台的前端页面开发、接口联调和组件维护。', '熟悉 Vue 3、TypeScript 和前端工程化，有管理后台经验优先。', 'OPEN', NOW(), NOW(), NOW()),
  (102, 3, 1, '后端开发工程师', '用友金融信息技术股份有限公司', '上海市', '技术研发', 'FULL_TIME', '金融科技', '310000', '310100', 'HYBRID', 'FULL_TIME', NULL, '["Java","Spring Boot","MySQL","Redis"]', '["Java","Spring Boot","微服务","接口设计"]', 260, 380, '负责金融科技平台服务开发、接口设计、数据建模与性能优化。', '扎实掌握 Java、Spring Boot、MySQL，了解缓存和消息机制。', 'OPEN', NOW(), NOW(), NOW()),
  (103, 3, 1, '测试开发工程师', '用友金融信息技术股份有限公司', '深圳市', '技术研发', 'FULL_TIME', '金融科技', '440000', '440300', 'HYBRID', 'FULL_TIME', NULL, '["Python","接口测试","自动化测试","CI/CD"]', '["自动化","测试平台","质量保障","Python"]', 230, 340, '建设自动化测试脚本和质量平台，支持核心业务持续交付。', '具备接口测试和自动化经验，熟悉 Python 或 Java 优先。', 'OPEN', NOW(), NOW(), NOW()),
  (104, 4, 1, '算法工程师（推荐方向）', '用友汽车信息科技（上海）股份有限公司', '杭州市', '技术研发', 'FULL_TIME', '汽车数智化', '330000', '330100', 'HYBRID', 'FULL_TIME', NULL, '["Python","机器学习","推荐系统","特征工程"]', '["算法","推荐","Python","机器学习"]', 280, 420, '参与推荐和匹配算法建设，支撑岗位推荐和人岗匹配能力。', '熟悉机器学习基础和 Python 工程实践，有推荐项目经验优先。', 'OPEN', NOW(), NOW(), NOW()),
  (105, 1, 1, 'Java 工程师（校招）', '用友网络科技股份有限公司', '北京市', '技术研发', 'FULL_TIME', '智能云平台', '110000', '110100', 'ONSITE', 'FULL_TIME', NULL, '["Java","Spring Cloud","MySQL","微服务"]', '["Java","校招","服务治理","云平台"]', 240, 360, '参与集团统一门户和招聘平台服务研发，支持业务系统迭代。', '具备良好的 Java 基础，理解微服务架构和数据库设计。', 'CLOSED', NOW(), NOW(), NOW()),
  (106, 5, 1, 'DevOps 开发工程师', '用友能源科技有限公司', '广州市', '技术研发', 'FULL_TIME', '能源数字化', '440000', '440100', 'ONSITE', 'FULL_TIME', NULL, '["Docker","Kubernetes","Linux","监控告警"]', '["DevOps","云原生","K8s","交付效率"]', 250, 360, '负责研发环境、发布流水线和监控告警平台建设。', '熟悉 Docker、Kubernetes 和 Linux 运维基础，有自动化交付经验优先。', 'DRAFT', NULL, NOW(), NOW()),

  (107, 5, 2, '产品经理实习生', '用友能源科技有限公司', '杭州市', '产品', 'SHORT_TERM', '能源数字化', '330000', '330100', 'HYBRID', 'SHORT_TERM', '2个月及以上', '["用户访谈","PRD","需求分析","流程梳理"]', '["产品实习","PRD","需求管理","业务流程"]', 170, 240, '协助产品经理梳理需求、绘制流程并跟进交付节奏。', '具备良好沟通能力和文档能力，对 B 端产品有兴趣。', 'OPEN', NOW(), NOW(), NOW()),
  (108, 1, 2, 'B端产品经理', '用友网络科技股份有限公司', '北京市', '产品', 'FULL_TIME', '智能云平台', '110000', '110100', 'ONSITE', 'FULL_TIME', NULL, '["需求分析","原型设计","产品规划","项目协同"]', '["B端产品","需求分析","原型","协同"]', 260, 380, '负责招聘平台和协同产品的需求规划、原型设计与跨团队推进。', '有 B 端产品经验，能独立输出 PRD 和高保真原型优先。', 'OPEN', NOW(), NOW(), NOW()),
  (109, 2, 2, '增长产品经理', '用友广信', '上海市', '产品', 'FULL_TIME', '政企数智服务', '310000', '310100', 'HYBRID', 'FULL_TIME', NULL, '["增长策略","数据分析","用户运营","A/B 测试"]', '["增长","产品策略","运营协同","数据驱动"]', 250, 360, '围绕用户增长和转化指标设计产品策略并联动运营落地。', '熟悉增长分析方法，有数据驱动意识和实验设计能力。', 'OPEN', NOW(), NOW(), NOW()),
  (110, 3, 2, 'AI 产品策划', '用友金融信息技术股份有限公司', '深圳市', '产品', 'FULL_TIME', '金融科技', '440000', '440300', 'HYBRID', 'FULL_TIME', NULL, '["AI 产品","Prompt 设计","场景拆解","需求分析"]', '["AI产品","场景设计","需求拆解","效率工具"]', 280, 400, '负责 AI 招聘和智能助手场景规划，推动从需求到交付闭环。', '理解 AI 产品交互和场景定义，有 B 端产品经验优先。', 'OPEN', NOW(), NOW(), NOW()),
  (111, 6, 2, '交付产品经理', '用友审计软件有限公司', '武汉市', '产品', 'FULL_TIME', '审计与风控', '420000', '420100', 'ONSITE', 'FULL_TIME', NULL, '["交付管理","需求澄清","流程设计","客户沟通"]', '["交付","客户需求","流程","项目推进"]', 230, 330, '支持审计和风控产品交付项目，负责需求澄清和流程设计。', '有客户沟通与交付协同经验优先，能适应多团队协作。', 'CLOSED', NOW(), NOW(), NOW()),
  (112, 2, 2, '产品培训生', '用友广信', '南京市', '产品', 'FULL_TIME', '政企数智服务', '320000', '320100', 'ONSITE', 'FULL_TIME', NULL, '["竞品分析","产品文档","需求管理","用户洞察"]', '["培训生","产品基础","分析","文档"]', 180, 260, '参与产品调研、需求整理和项目协同，作为产品培训生培养。', '有逻辑分析能力和较强学习意愿，专业不限。', 'DRAFT', NULL, NOW(), NOW()),

  (113, 4, 3, '数据分析实习生', '用友汽车信息科技（上海）股份有限公司', '上海市', '数据', 'LONG_TERM', '汽车数智化', '310000', '310100', 'ONSITE', 'LONG_TERM', '3个月及以上', '["SQL","Python","Tableau","数据建模"]', '["数据分析","SQL","Tableau","经营看板"]', 180, 260, '支持业务看板、投递分析和经营数据洞察，输出分析报告。', '熟悉 SQL 和 Python，具备可视化分析经验优先。', 'OPEN', NOW(), NOW(), NOW()),
  (114, 6, 3, 'BI 分析师', '用友审计软件有限公司', '武汉市', '数据', 'FULL_TIME', '审计与风控', '420000', '420100', 'ONSITE', 'FULL_TIME', NULL, '["SQL","Power BI","指标体系","数据治理"]', '["BI","指标体系","经营分析","数据治理"]', 220, 320, '负责搭建经营 BI 指标体系和多角色分析看板。', '具备 SQL、Power BI 或同类工具经验，能独立抽象指标。', 'OPEN', NOW(), NOW(), NOW()),
  (115, 5, 3, '经营分析师', '用友能源科技有限公司', '广州市', '数据', 'FULL_TIME', '能源数字化', '440000', '440100', 'HYBRID', 'FULL_TIME', NULL, '["Excel","SQL","经营分析","数据洞察"]', '["经营分析","报表","指标","业务洞察"]', 220, 320, '结合业务目标搭建经营分析框架，产出月度和专题分析。', '理解经营分析方法，具备 SQL 和 Excel 能力。', 'OPEN', NOW(), NOW(), NOW()),
  (116, 1, 3, '数据治理工程师', '用友网络科技股份有限公司', '北京市', '数据', 'FULL_TIME', '智能云平台', '110000', '110100', 'ONSITE', 'FULL_TIME', NULL, '["数据治理","元数据","质量规则","SQL"]', '["数据治理","元数据","质量管理","平台"]', 240, 350, '参与数据标准、质量规则和元数据平台建设。', '有数据治理或数据仓库项目经验优先，理解数据质量治理流程。', 'OPEN', NOW(), NOW(), NOW()),
  (117, 2, 3, '商业分析师', '用友广信', '成都市', '数据', 'FULL_TIME', '政企数智服务', '510000', '510100', 'HYBRID', 'FULL_TIME', NULL, '["商业分析","行业研究","SQL","报告撰写"]', '["商业分析","行业研究","咨询","数据"]', 210, 300, '围绕政企客户项目输出行业和业务分析结论，支持方案制定。', '具备行业研究或咨询分析经验优先。', 'CLOSED', NOW(), NOW(), NOW()),
  (118, 4, 3, '数据产品分析师', '用友汽车信息科技（上海）股份有限公司', '杭州市', '数据', 'FULL_TIME', '汽车数智化', '330000', '330100', 'HYBRID', 'FULL_TIME', NULL, '["数据分析","埋点设计","SQL","产品分析"]', '["数据产品","埋点","分析","增长"]', 220, 320, '负责产品埋点设计、漏斗分析和关键指标监控。', '理解产品分析方法，能与产品和研发协同推进。', 'DRAFT', NULL, NOW(), NOW()),

  (119, 2, 4, 'UI 设计实习生', '用友广信', '南京市', '设计', 'SHORT_TERM', '政企数智服务', '320000', '320100', 'REMOTE', 'SHORT_TERM', '2个月及以上', '["Figma","UI","视觉设计","信息设计"]', '["UI设计","Figma","视觉","设计规范"]', 160, 230, '支持产品页面、活动物料和设计规范维护。', '有设计作品集，熟练使用 Figma 或 Sketch。', 'OPEN', NOW(), NOW(), NOW()),
  (120, 1, 4, '交互设计师', '用友网络科技股份有限公司', '北京市', '设计', 'FULL_TIME', '智能云平台', '110000', '110100', 'ONSITE', 'FULL_TIME', NULL, '["交互设计","原型","用户体验","Figma"]', '["交互","用户体验","原型","设计系统"]', 240, 340, '负责招聘与协同产品交互方案设计和体验优化。', '具备完整交互设计项目经验，理解 B 端设计逻辑。', 'OPEN', NOW(), NOW(), NOW()),
  (121, 4, 4, '视觉设计师', '用友汽车信息科技（上海）股份有限公司', '上海市', '设计', 'FULL_TIME', '汽车数智化', '310000', '310100', 'HYBRID', 'FULL_TIME', NULL, '["视觉设计","品牌延展","海报","Figma"]', '["视觉","品牌","活动物料","创意"]', 220, 320, '输出活动物料、品牌视觉和产品配图，提升整体视觉质量。', '具备品牌或营销视觉设计经验，有成体系作品集优先。', 'OPEN', NOW(), NOW(), NOW()),
  (122, 3, 4, '体验设计师', '用友金融信息技术股份有限公司', '深圳市', '设计', 'FULL_TIME', '金融科技', '440000', '440300', 'HYBRID', 'FULL_TIME', NULL, '["体验设计","服务设计","用户研究","原型"]', '["体验设计","研究","服务设计","金融产品"]', 250, 350, '围绕金融科技产品体验进行流程设计、研究和迭代。', '具备体验设计或用户研究背景，能独立推进设计方案。', 'OPEN', NOW(), NOW(), NOW()),
  (123, 5, 4, '品牌设计师', '用友能源科技有限公司', '广州市', '设计', 'FULL_TIME', '能源数字化', '440000', '440100', 'ONSITE', 'FULL_TIME', NULL, '["品牌设计","平面设计","活动视觉","创意"]', '["品牌","平面","活动视觉","创意"]', 220, 320, '负责品牌传播、活动视觉和内容包装设计。', '具备品牌视觉经验，擅长平面和线上活动物料设计。', 'CLOSED', NOW(), NOW(), NOW()),
  (124, 2, 4, '设计培训生', '用友广信', '杭州市', '设计', 'FULL_TIME', '政企数智服务', '330000', '330100', 'ONSITE', 'FULL_TIME', NULL, '["设计基础","排版","视觉表达","Figma"]', '["培训生","视觉基础","排版","学习能力"]', 170, 240, '参与设计任务支持和规范整理，作为设计培训生培养。', '具备设计基础和作品样例，乐于学习和协作。', 'DRAFT', NULL, NOW(), NOW()),

  (125, 6, 5, '市场运营实习生', '用友审计软件有限公司', '武汉市', '市场运营', 'LONG_TERM', '审计与风控', '420000', '420100', 'ONSITE', 'LONG_TERM', '3个月及以上', '["内容策划","社群运营","数据复盘","活动执行"]', '["市场运营","内容","活动","社群"]', 170, 230, '协助活动执行、内容策划和渠道运营复盘。', '具备文字表达和活动执行能力，对 ToB 市场有兴趣。', 'OPEN', NOW(), NOW(), NOW()),
  (126, 1, 5, '招聘运营专员', '用友网络科技股份有限公司', '北京市', '市场运营', 'FULL_TIME', '智能云平台', '110000', '110100', 'ONSITE', 'FULL_TIME', NULL, '["招聘运营","渠道管理","数据分析","流程协同"]', '["招聘运营","流程","数据","渠道"]', 220, 320, '负责招聘流程协同、数据分析和渠道维护。', '熟悉招聘协同流程，有数据整理与沟通推进能力。', 'OPEN', NOW(), NOW(), NOW()),
  (127, 2, 5, '内容运营经理', '用友广信', '上海市', '市场运营', 'FULL_TIME', '政企数智服务', '310000', '310100', 'HYBRID', 'FULL_TIME', NULL, '["内容运营","选题策划","新媒体","数据复盘"]', '["内容运营","选题","传播","数据复盘"]', 230, 330, '负责品牌内容策划、专题传播和内容效果分析。', '有内容运营或品牌传播经验，善于跨部门协同。', 'OPEN', NOW(), NOW(), NOW()),
  (128, 5, 5, '用户增长运营', '用友能源科技有限公司', '深圳市', '市场运营', 'FULL_TIME', '能源数字化', '440000', '440300', 'HYBRID', 'FULL_TIME', NULL, '["增长运营","用户分层","活动策略","数据分析"]', '["增长运营","用户分层","活动","转化"]', 240, 340, '围绕用户增长目标设计活动和转化策略，持续优化运营链路。', '具备增长运营经验，擅长数据分析和活动复盘。', 'OPEN', NOW(), NOW(), NOW()),
  (129, 3, 5, '品牌市场专员', '用友金融信息技术股份有限公司', '成都市', '市场运营', 'FULL_TIME', '金融科技', '510000', '510100', 'ONSITE', 'FULL_TIME', NULL, '["品牌传播","活动执行","渠道投放","复盘"]', '["品牌市场","活动","传播","渠道"]', 210, 300, '支持品牌活动、渠道传播和市场项目落地。', '有市场活动或传播经验优先，执行力强。', 'CLOSED', NOW(), NOW(), NOW()),
  (130, 2, 5, '活动运营培训生', '用友广信', '杭州市', '市场运营', 'FULL_TIME', '政企数智服务', '330000', '330100', 'ONSITE', 'FULL_TIME', NULL, '["活动运营","内容编辑","沟通协同","执行"]', '["培训生","活动执行","内容","协同"]', 170, 250, '协助活动策划、嘉宾沟通和现场执行，作为运营培训生培养。', '有校园活动或组织经验优先，沟通能力良好。', 'DRAFT', NULL, NOW(), NOW());

INSERT INTO guide_doc (id, code, title, file_url, sort_num, create_time, update_time)
VALUES
  (201, 'resume-guide', '集团简历填写指南', '/pdf/resume-guide.pdf', 1, NOW(), NOW()),
  (202, 'interview-guide', '集团面试准备指南', '/pdf/interview-guide.pdf', 2, NOW(), NOW()),
  (203, 'portfolio-guide', '集团项目展示指南', '/pdf/portfolio-guide.pdf', 3, NOW(), NOW());

INSERT INTO resume (id, user_id, title, resume_name, resume_type, target_subsidiary_id, target_category_id, basic_info_json, education_json, experience_json, project_json, skills_json, template_code, pdf_url, complete_score, is_default, create_time, update_time)
VALUES
  (301, 1, '前端开发岗简历', '前端开发岗简历', 'TARGETED', 1, 1, '{"name":"张晨","phone":"13800000000","email":"student01@example.com","educationLevel":"本科","gender":"男"}', '[{"school":"北京邮电大学","educationStage":"本科","major":"软件工程","startDate":"2022-09","endDate":"2026-06","courses":"Web 前端开发、软件工程、数据库系统"}]', '[{"company":"校园技术中心","position":"前端开发实习生","employmentType":"实习","startDate":"2025-07","endDate":"2025-09","content":"负责 Vue 3 管理后台开发、组件封装与接口联调。"}]', '[{"name":"集团招聘门户演示项目","role":"前端负责人","startDate":"2025-03","endDate":"2025-06","content":"完成岗位检索、投递流程和简历管理等核心模块开发。"}]', '["Vue 3","TypeScript","Element Plus","Axios","工程化"]', 'classic', '/files/resume/301.pdf', 100, 1, NOW(), NOW()),
  (302, 1, '数据分析岗简历', '数据分析岗简历', 'TARGETED', 4, 3, '{"name":"张晨","phone":"13800000000","email":"student01@example.com","educationLevel":"本科","gender":"男"}', '[{"school":"北京邮电大学","educationStage":"本科","major":"信息管理与信息系统","startDate":"2022-09","endDate":"2026-06","courses":"统计学、数据分析、数据库原理"}]', '[{"company":"数据实验室","position":"数据分析实习生","employmentType":"实习","startDate":"2025-03","endDate":"2025-06","content":"负责 SQL 报表开发、经营看板分析和复盘报告撰写。"}]', '[{"name":"汽车行业经营看板","role":"数据分析","startDate":"2025-01","endDate":"2025-04","content":"使用 SQL 和 Tableau 搭建经营分析看板并输出洞察。"}]', '["SQL","Python","Tableau","Excel","数据建模"]', 'fresh', '/files/resume/302.pdf', 100, 0, NOW(), NOW()),
  (303, 2, '通用版简历', '通用版简历', 'GENERAL', NULL, 1, '{"name":"李欣","phone":"13800000001","email":"student02@example.com","educationLevel":"本科","gender":"女"}', '[{"school":"华南理工大学","educationStage":"本科","major":"计算机科学与技术","startDate":"2021-09","endDate":"2025-06","courses":"计算机网络、操作系统、软件测试"}]', '[{"company":"金融科技实验室","position":"后端开发实习生","employmentType":"实习","startDate":"2024-12","endDate":"2025-03","content":"参与 Java 服务开发、接口测试与数据库设计。"}]', '[{"name":"统一权限平台","role":"后端开发","startDate":"2024-09","endDate":"2024-12","content":"基于 Spring Boot 完成统一认证、角色权限与日志模块。"}]', '["Java","Spring Boot","MySQL","Redis","接口设计"]', 'sharp', '/files/resume/303.pdf', 100, 1, NOW(), NOW());

INSERT INTO favorite_job (id, user_id, job_id, create_time)
VALUES
  (401, 1, 101, NOW()),
  (402, 1, 113, NOW()),
  (403, 2, 102, NOW());

INSERT INTO application_record (id, user_id, job_id, resume_id, stage, hr_note, match_score, system_match_score, hr_review_score, hr_review_status, hr_review_time, apply_time, update_time)
VALUES
  (501, 1, 101, 301, 'APPLIED', '技术栈匹配度较高，等待安排初筛。', 94, 94, NULL, 'PENDING', NULL, NOW(), NOW()),
  (502, 1, 113, 302, 'INTERVIEW', '分析思路清晰，进入业务面试。', 88, 88, 90, '建议推进', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),
  (503, 2, 102, 303, 'SCREENING', '后端基础扎实，等待技术负责人确认。', 91, 91, 86, '建议推进', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), NOW()),
  (504, 2, 126, 303, 'OFFER', '综合评价稳定，建议发放录用。', 80, 80, 88, '建议推进', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), NOW()),
  (505, 1, 107, 302, 'REJECTED', '岗位方向匹配度一般。', 72, 72, 70, '不推荐', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), NOW());

INSERT INTO interview_record (id, application_id, round_num, interview_time, interviewer, status, remark, create_time, update_time)
VALUES
  (601, 502, 1, DATE_ADD(NOW(), INTERVAL 2 DAY), '王老师', 'PENDING', '已安排集团业务面试。', NOW(), NOW()),
  (602, 503, 1, DATE_ADD(NOW(), INTERVAL 3 DAY), '张经理', 'PLANNED', '待发送会议链接。', NOW(), NOW());
