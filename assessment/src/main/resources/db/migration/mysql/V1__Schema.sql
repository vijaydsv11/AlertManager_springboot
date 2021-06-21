CREATE TABLE IF NOT EXISTS user_data (
    id bigint not null auto_increment,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_firstname VARCHAR(255) NOT NULL,
    user_lastname VARCHAR(255),
    user_middlename VARCHAR(255),
    user_email VARCHAR(255) NOT NULL,
    employee_id VARCHAR(255),
    department VARCHAR(255),
    phone_no VARCHAR(15),
    invalid_auth_req_count INTEGER,
	last_password_reset TIMESTAMP,
    datecreated timestamp DEFAULT CURRENT_TIMESTAMP,
    role_id BIGINT,
    PRIMARY KEY (id)
);

 CREATE TABLE IF NOT EXISTS role (
    id BIGINT NOT NULL,
    role_name VARCHAR(255) NOT NULL,
    status varchar(50),
    dateCreated TIMESTAMP,
    PRIMARY KEY (id)
);

 CREATE TABLE IF NOT EXISTS module (
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    dateCreated TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS alert_review (
    id BIGINT NOT NULL AUTO_INCREMENT,
    alert_review_id VARCHAR(255) NOT NULL,
    score VARCHAR(100),
    focus_type VARCHAR(100),
    focus VARCHAR(100),
    customer_name VARCHAR(255),
    created VARCHAR(50),
    status VARCHAR(100),
    threshold_name VARCHAR(100),
    risk_score VARCHAR(100),
    employee_name VARCHAR(255),
	customer_id VARCHAR(100),
    datecreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS complaince_risk (
    id bigint not null auto_increment,
    sno VARCHAR(100),
    add_mod_del VARCHAR(100),
    comp_officer VARCHAR(100),
    circular_Ref_No VARCHAR(100),
    circular_date VARCHAR(50),
    circular_name VARCHAR(255),
    regulator VARCHAR(100),
    risk_score VARCHAR(100),
    cluster VARCHAR(100),
	reg_guidlines VARCHAR(255),
    control_desc VARCHAR(255),
    risk_owner VARCHAR(255),
    risk_sub_unit VARCHAR(255),
    control_owner VARCHAR(255),
    control_sub_unit VARCHAR(255),
    prod_func VARCHAR(255),
    prod_name VARCHAR(255),
    prod_launch_dt VARCHAR(255),
    prod_testing VARCHAR(255),
    prod_testing_dt VARCHAR(255),
    board_app_policy VARCHAR(255),
    relevant_policy VARCHAR(255),
    process_note VARCHAR(255),
    like_rating VARCHAR(255),
    impact VARCHAR(255),
    monitor_mechanism VARCHAR(255),
    control_automation  VARCHAR(255),
    control_effective VARCHAR(255),
    comp_risk VARCHAR(255),
    breach VARCHAR(255),
    process_improvement VARCHAR(255),
    details_breach VARCHAR(255),
    details_process_mprovement VARCHAR(255),
    timeline VARCHAR(255),
    spoc VARCHAR(255),
    manco_member VARCHAR(255),
    datecreated timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_module (
    id BIGINT NOT NULL AUTO_INCREMENT,
    module_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (id)
)  ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS user_alert_review (
    id BIGINT NOT NULL AUTO_INCREMENT,
    alert_id BIGINT,
    user_id BIGINT,
	end_time TIMESTAMP,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_by varchar(100),
    assigned_to varchar(100),
    status varchar(100),
    PRIMARY KEY (id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS user_complaince_risk (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cr_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (id)
)  ENGINE=INNODB;


alter table user_data add constraint UK_user_username unique (username);
alter table user_data add constraint FK_user_REF_role_id foreign key (role_id) references role (id);

alter table user_module add constraint UK_user_module_userid_modid unique (user_id, module_id);
alter table user_module add constraint FK_user_module_REF_module_id foreign key (module_id) references module (id);
alter table user_module add constraint FK_user_module_REF_user_id foreign key (user_id) references user_data (id);

alter table user_alert_review add constraint UK_user_alert_userid_alertid unique (user_id, alert_id);
alter table user_alert_review add constraint FK_user_alert_REF_alert_id foreign key (alert_id) references alert_review (id);
alter table user_alert_review add constraint FK_user_alert_REF_user_id foreign key (user_id) references user_data (id);


alter table user_complaince_risk add constraint UK_user_cr_userid_crid unique (user_id, cr_id);
alter table user_complaince_risk add constraint FK_user_complaince_risk_REF_cr_id foreign key (cr_id) references complaince_risk (id);
alter table user_complaince_risk add constraint FK_user_complaince_risk_REF_user_id foreign key (user_id) references user_data (id);


