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
  (1, '用友集团', '用友网络科技股份有限公司', '用友网络', '智能云平台', '集团核心数字化平台主体，承接企业云服务与产品研发。', 1, 1, NOW(), NOW()),
  (2, '用友集团', '用友广信', '用友广信', '政企数智服务', '聚焦区域政企客户数字化建设与运营服务。', 1, 2, NOW(), NOW()),
  (3, '用友集团', '用友金融信息技术股份有限公司', '用友金融', '金融科技', '面向银行、保险与证券行业提供金融科技解决方案。', 1, 3, NOW(), NOW()),
  (4, '用友集团', '用友汽车信息科技（上海）股份有限公司', '用友汽车', '汽车数智化', '围绕汽车产业链提供业务协同与数据中台能力。', 1, 4, NOW(), NOW()),
  (5, '用友集团', '用友能源科技有限公司', '用友能源', '能源数字化', '为能源企业提供生产经营一体化数字平台。', 1, 5, NOW(), NOW()),
  (6, '用友集团', '用友审计软件有限公司', '用友审计', '审计与风控', '服务集团与客户的审计、风控与合规数字化建设。', 1, 6, NOW(), NOW());

INSERT INTO job_category (id, category_name, parent_id, sort_num, status, create_time, update_time)
VALUES
  (1, '技术研发', NULL, 1, 1, NOW(), NOW()),
  (2, '产品运营', NULL, 2, 1, NOW(), NOW()),
  (3, '数据分析', NULL, 3, 1, NOW(), NOW()),
  (4, '设计创意', NULL, 4, 1, NOW(), NOW()),
  (5, '市场职能', NULL, 5, 1, NOW(), NOW());

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
  (101, 1, 1, '集团前端开发实习生', '用友网络科技股份有限公司', '北京市', '技术研发', 'LONG_TERM', '智能云平台', '110000', '110100', 'ONSITE', 'LONG_TERM', '3个月及以上', '["Vue 3", "TypeScript", "Element Plus", "工程化"]', '["Vue 3", "TypeScript", "Element Plus", "工程化"]', 180, 260, '参与集团统一招聘门户与内部招聘运营平台前端模块建设。', '熟悉 Vue 3、TypeScript、组件化开发，有后台系统项目经验优先。', 'OPEN', NOW(), NOW(), NOW()),
  (102, 3, 1, '金融科技后端开发实习生', '用友金融信息技术股份有限公司', '上海市', '技术研发', 'LONG_TERM', '金融科技', '310000', '310100', 'HYBRID', 'LONG_TERM', '4个月及以上', '["Java", "Spring Boot", "MySQL", "Redis"]', '["Java", "Spring Boot", "MySQL", "Redis"]', 200, 300, '参与金融业务中台接口开发与数据服务治理。', '熟悉 Java、Spring Boot、MySQL，了解缓存和消息机制优先。', 'OPEN', NOW(), NOW(), NOW()),
  (103, 4, 3, '汽车行业数据分析实习生', '用友汽车信息科技（上海）股份有限公司', '上海市', '数据分析', 'LONG_TERM', '汽车数智化', '310000', '310100', 'ONSITE', 'LONG_TERM', '3个月及以上', '["SQL", "Python", "Tableau", "数据建模"]', '["SQL", "Python", "Tableau", "数据建模"]', 180, 260, '支持汽车客户业务数据分析、投递转化分析与经营看板制作。', '熟悉 SQL 与 Python，具备可视化分析经验。', 'OPEN', NOW(), NOW(), NOW()),
  (104, 5, 2, '能源行业产品运营实习生', '用友能源科技有限公司', '杭州市', '产品运营', 'SHORT_TERM', '能源数字化', '330000', '330100', 'HYBRID', 'SHORT_TERM', '2个月及以上', '["用户研究", "PRD", "流程梳理"]', '["用户研究", "PRD", "流程梳理"]', 170, 240, '协助能源数字化产品进行需求梳理、运营活动策划与数据跟踪。', '具备良好沟通能力和文档能力，对企业软件产品有兴趣。', 'OPEN', NOW(), NOW(), NOW()),
  (105, 2, 4, '政企业务视觉设计实习生', '用友广信', '南京市', '设计创意', 'SHORT_TERM', '政企数智服务', '320000', '320100', 'REMOTE', 'SHORT_TERM', '2个月及以上', '["Figma", "UI", "信息设计"]', '["Figma", "UI", "信息设计"]', 160, 230, '负责集团政企业务方案页面、活动页面与视觉规范设计支持。', '熟练使用 Figma，有设计作品集。', 'OPEN', NOW(), NOW(), NOW()),
  (106, 6, 5, '审计风控数据治理实习生', '用友审计软件有限公司', '武汉市', '市场职能', 'LONG_TERM', '审计与风控', '420000', '420100', 'ONSITE', 'LONG_TERM', '3个月及以上', '["Excel", "SQL", "流程管理"]', '["Excel", "SQL", "流程管理"]', 170, 220, '支持审计与风控业务的数据治理、流程校验与材料归档。', '细致认真，具备基础 SQL 与数据处理能力。', 'OPEN', NOW(), NOW(), NOW()),
  (107, 1, 1, '集团 Java 开发工程师（校招）', '用友网络科技股份有限公司', '北京市', '技术研发', 'FULL_TIME', '智能云平台', '110000', '110100', 'ONSITE', 'FULL_TIME', NULL, '["Java", "Spring Cloud", "MySQL", "微服务"]', '["Java", "Spring Cloud", "MySQL", "微服务"]', 260, 420, '参与集团核心招聘与人力数字化平台的后端服务开发。', '扎实的 Java 基础，理解微服务架构和数据库设计。', 'DRAFT', NULL, NOW(), NOW()),
  (108, 3, 1, '金融平台测试开发工程师', '用友金融信息技术股份有限公司', '深圳市', '技术研发', 'FULL_TIME', '金融科技', '440000', '440300', 'HYBRID', 'FULL_TIME', NULL, '["测试开发", "Java", "接口测试", "自动化"]', '["测试开发", "Java", "接口测试", "自动化"]', 240, 360, '负责金融平台质量体系建设与自动化测试能力提升。', '有自动化测试项目经验，了解 Java 或 Python。', 'CLOSED', NOW(), NOW(), NOW());

INSERT INTO guide_doc (id, code, title, file_url, sort_num, create_time, update_time)
VALUES
  (201, 'resume-guide', '集团简历填写指南', '/pdf/resume-guide.pdf', 1, NOW(), NOW()),
  (202, 'interview-guide', '集团面试准备指南', '/pdf/interview-guide.pdf', 2, NOW(), NOW()),
  (203, 'portfolio-guide', '集团项目展示指南', '/pdf/portfolio-guide.pdf', 3, NOW(), NOW());

INSERT INTO resume (id, user_id, title, resume_name, resume_type, target_subsidiary_id, target_category_id, basic_info_json, education_json, experience_json, project_json, skills_json, template_code, pdf_url, complete_score, is_default, create_time, update_time)
VALUES
  (301, 1, '前端开发岗简历', '前端开发岗简历', 'TARGETED', 1, 1, '{"name":"张晨","phone":"13800000000","email":"student01@example.com","school":"北京邮电大学","major":"软件工程","intention":"前端开发 / 平台研发"}', '[{"school":"北京邮电大学","degree":"本科","major":"软件工程","startDate":"2022-09","endDate":"2026-06"}]', '[{"company":"校园技术中心","position":"前端开发实习生","startDate":"2025-07","endDate":"2025-09","description":"负责 Vue 3 管理后台开发与接口联调。"}]', '[{"name":"集团招聘门户演示项目","role":"前端负责人","description":"完成岗位检索、投递流程、简历管理等核心模块开发。"}]', '["Vue 3", "TypeScript", "Element Plus", "Axios", "工程化"]', 'classic', '/files/resume/201.pdf', 100, 1, NOW(), NOW()),
  (302, 1, '数据分析岗简历', '数据分析岗简历', 'TARGETED', 4, 3, '{"name":"张晨","phone":"13800000000","email":"student01@example.com","school":"北京邮电大学","major":"信息管理与信息系统","intention":"数据分析 / 业务分析"}', '[{"school":"北京邮电大学","degree":"本科","major":"信息管理与信息系统","startDate":"2022-09","endDate":"2026-06"}]', '[{"company":"数据实验室","position":"数据分析实习生","startDate":"2025-03","endDate":"2025-06","description":"负责 SQL 报表开发与看板分析。"}]', '[{"name":"汽车行业经营看板","role":"数据分析","description":"使用 SQL 和 Tableau 搭建投递与经营分析看板。"}]', '["SQL", "Python", "Tableau", "Excel", "数据建模"]', 'fresh', '/files/resume/202.pdf', 100, 0, NOW(), NOW()),
  (303, 2, '通用版简历', '通用版简历', 'GENERAL', NULL, 1, '{"name":"李欣","phone":"13800000001","email":"student02@example.com","school":"华南理工大学","major":"计算机科学与技术","intention":"后端开发 / 平台研发"}', '[{"school":"华南理工大学","degree":"本科","major":"计算机科学与技术","startDate":"2021-09","endDate":"2025-06"}]', '[{"company":"金融科技实验室","position":"后端开发实习生","startDate":"2024-12","endDate":"2025-03","description":"参与 Java 服务开发、接口测试与数据库设计。"}]', '[{"name":"统一权限平台","role":"后端开发","description":"基于 Spring Boot 完成统一认证、角色权限与日志模块。"}]', '["Java", "Spring Boot", "MySQL", "Redis", "接口设计"]', 'sharp', '/files/resume/default-resume.pdf', 100, 1, NOW(), NOW());

INSERT INTO favorite_job (id, user_id, job_id, create_time)
VALUES
  (401, 1, 101, NOW()),
  (402, 1, 103, NOW()),
  (403, 2, 102, NOW());

INSERT INTO application_record (id, user_id, job_id, resume_id, stage, hr_note, match_score, system_match_score, hr_review_score, hr_review_status, hr_review_time, apply_time, update_time)
VALUES
  (501, 1, 101, 301, 'APPLIED', '技术栈匹配度较高，待安排初筛。', 94, 94, NULL, 'PENDING', NULL, NOW(), NOW()),
  (502, 1, 103, 302, 'INTERVIEW', '分析思路清晰，进入业务面试。', 88, 88, 90, '推荐进入复试', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),
  (503, 2, 102, 303, 'SCREENING', '后端基础扎实，等待技术负责人确认。', 91, 91, 86, '初筛通过', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), NOW()),
  (504, 2, 106, 303, 'OFFER', '综合评价稳定，建议发放录用。', 80, 80, 88, '已发录用', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), NOW()),
  (505, 1, 104, 302, 'REJECTED', '岗位方向匹配度一般。', 72, 72, 70, '未通过', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), NOW());

INSERT INTO interview_record (id, application_id, round_num, interview_time, interviewer, status, remark, create_time, update_time)
VALUES
  (601, 502, 1, DATE_ADD(NOW(), INTERVAL 2 DAY), '王老师', 'PENDING', '已安排集团业务面试。', NOW(), NOW()),
  (602, 503, 1, DATE_ADD(NOW(), INTERVAL 3 DAY), '张经理', 'PLANNED', '待发送会议链接。', NOW(), NOW());
