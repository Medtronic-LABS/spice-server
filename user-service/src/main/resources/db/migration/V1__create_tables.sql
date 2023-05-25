CREATE TABLE organization (
  id SERIAL PRIMARY KEY,
  form_data_id BIGINT,
  form_name VARCHAR,
  name VARCHAR,
  sequence BIGINT,
  parent_organization_id BIGINT, FOREIGN KEY (parent_organization_id) REFERENCES organization(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE country (
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL,
  country_code VARCHAR NOT NULL,
  unit_measurement VARCHAR NOT NULL,
  tenant_id BIGINT,
  created_by BIGINT NOT NULL, 
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE timezone (
  id SERIAL PRIMARY KEY,
  description VARCHAR,
  abbreviation VARCHAR,
  "offset" VARCHAR,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);


CREATE TABLE "user" (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR,
  last_name VARCHAR,
  middle_name VARCHAR,
  gender VARCHAR,
  phone_number VARCHAR,
  address VARCHAR,
  username VARCHAR,
  password VARCHAR,
  country_code VARCHAR,
  is_blocked BOOLEAN,
  blocked_date timestamp,
  forget_password_token VARCHAR,
  forget_password_time TIMESTAMP DEFAULT (CURRENT_TIMESTAMP),
  forget_password_count INT,
  invalid_login_attempts INT,
  invalid_login_time timestamptz DEFAULT (CURRENT_TIMESTAMP),
  invalid_reset_time timestamptz DEFAULT (CURRENT_TIMESTAMP),
  is_password_reset_enabled BOOLEAN,
  password_reset_attempts INT,
  is_license_acceptance BOOLEAN,
  last_logged_in TIMESTAMP,
  last_logged_out TIMESTAMP,
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  timezone_id BIGINT, FOREIGN KEY (timezone_id) REFERENCES timezone(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false,
  culture_id BIGINT
);

CREATE TABLE role (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  level INT,
  created_by BIGINT,
  updated_by BIGINT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE user_role (
  user_id BIGINT, FOREIGN KEY (user_id) REFERENCES "user"(id),
  role_id BIGINT, FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE user_organization (
  user_id BIGINT, FOREIGN KEY (user_id) REFERENCES "user"(id),
  organization_id BIGINT, FOREIGN KEY (organization_id) REFERENCES organization(id)
);


CREATE TABLE county (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  display_order INT,
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE sub_county (
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL,
  display_order INT,
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  county_id BIGINT, FOREIGN KEY (county_id) REFERENCES county(id),
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE clinical_workflow (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  workflow VARCHAR,
  module_type VARCHAR,
  view_screens TEXT[],
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  tenant_id BIGINT,
  is_default BOOLEAN DEFAULT false,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE account (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  is_users_restricted BOOLEAN,
  max_no_of_users INT,
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false,
  reason VARCHAR,
  status VARCHAR
);

CREATE TABLE account_clinical_workflow (
  account_id BIGINT, FOREIGN KEY (account_id) REFERENCES account(id),
  clinical_workflow_id BIGINT, FOREIGN KEY (clinical_workflow_id) REFERENCES clinical_workflow(id)
);

CREATE TABLE account_customized_workflow (
  account_id BIGINT, FOREIGN KEY (account_id) REFERENCES account(id),
  customized_workflow_id BIGINT, FOREIGN KEY (customized_workflow_id) REFERENCES clinical_workflow(id)
);

CREATE TABLE operating_unit (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  account_id BIGINT, FOREIGN KEY (account_id) REFERENCES account(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE culture (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  code VARCHAR,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE program (
  id SERIAL PRIMARY KEY,
  name VARCHAR NOT NULL,
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE site (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  email VARCHAR,
  address_type VARCHAR,
  address_use VARCHAR,
  address_1 VARCHAR,
  address_2 VARCHAR,
  latitude VARCHAR,
  longitude VARCHAR,
  city VARCHAR,
  phone_number VARCHAR,
  working_hours float,
  postal_code varchar,
  site_type VARCHAR,
  site_level VARCHAR,
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  county_id BIGINT, FOREIGN KEY (county_id) REFERENCES county(id),
  sub_county_id BIGINT, FOREIGN KEY (sub_county_id) REFERENCES sub_county(id),
  account_id BIGINT, FOREIGN KEY (account_id) REFERENCES account(id),
  operating_unit_id BIGINT, FOREIGN KEY (operating_unit_id) REFERENCES operating_unit(id),
  culture_id BIGINT, FOREIGN KEY (culture_id) REFERENCES culture(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE site_program (
    site_id BIGINT, FOREIGN KEY (site_id) REFERENCES site(id),
    program_id BIGINT, FOREIGN KEY (program_id) REFERENCES program(id)
);

CREATE TABLE deleted_site_program (
    site_id BIGINT, FOREIGN KEY (site_id) REFERENCES site(id),
    program_id BIGINT, FOREIGN KEY (program_id) REFERENCES program(id)
);


CREATE TABLE unit (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  type VARCHAR,
  description VARCHAR,
  display_order INT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE lab_test (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  is_result_template BOOLEAN,
  display_order INT,
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE lab_test_result (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  display_order INT,
  lab_test_id BIGINT, FOREIGN KEY (lab_test_id) REFERENCES lab_test(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE lab_test_result_range (
  id SERIAL PRIMARY KEY,
  minimum_value float,
  maximum_value float,
  display_order INT,
  unit VARCHAR,
  display_name VARCHAR,
  lab_test_id BIGINT, FOREIGN KEY (lab_test_id) REFERENCES lab_test(id),
  lab_test_result_id BIGINT, FOREIGN KEY (lab_test_result_id) REFERENCES lab_test_result(id),
  unit_id BIGINT, FOREIGN KEY (unit_id) REFERENCES unit(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient (
  id SERIAL PRIMARY KEY,
  national_id VARCHAR,
  first_name VARCHAR,
  middle_name VARCHAR,
  last_Name VARCHAR,
  gender VARCHAR,
  date_of_birth TIMESTAMP,
  age INT,
  is_pregnant BOOLEAN,
  phone_number VARCHAR,
  phone_number_category VARCHAR,
  landmark VARCHAR,
  occupation VARCHAR,
  level_of_education VARCHAR,
  insurance_status boolean,
  insurance_type VARCHAR,
  other_insurance VARCHAR,
  insurance_id VARCHAR,
  is_support_group BOOLEAN,
  support_group VARCHAR,
  is_regular_smoker BOOLEAN,
  initial VARCHAR,
  other_id_type VARCHAR,
  ethnicity VARCHAR,
  languages VARCHAR,
  id_type VARCHAR,
  other_languages VARCHAR,
  er_visit_reason VARCHAR,
  lote BOOLEAN,
  home_medical_devices VARCHAR,
  er_visit_frequency int,
  emr_number BIGINT,
  is_er_visit_history BOOLEAN,
  virtual_id BIGINT,
  zip_code varchar,
  site_id BIGINT, FOREIGN KEY (site_id) REFERENCES site(id),
  program_id BIGINT, FOREIGN KEY (program_id) REFERENCES program(id),
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  county_id BIGINT, FOREIGN KEY (county_id) REFERENCES county(id),
  sub_county_id BIGINT, FOREIGN KEY (sub_county_id) REFERENCES sub_county(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE screening_log (
  id SERIAL PRIMARY KEY,
  latitude VARCHAR,
  longitude VARCHAR,
  first_name VARCHAR,
  middle_name VARCHAR,
  last_Name VARCHAR,
  national_id VARCHAR,
  phone_number VARCHAR,
  phone_number_category VARCHAR,
  landmark VARCHAR,
  gender VARCHAR,
  date_of_birth TIMESTAMP,
  age INT,
  height FLOAT,
  weight FLOAT,
  bmi FLOAT,
  is_regular_smoker BOOLEAN,
  device_info_id BIGINT,
  category VARCHAR,
  avg_systolic INT,
  avg_diastolic INT,
  avg_pulse INT,
  glucose_type VARCHAR,
  glucose_value FLOAT,
  glucose_unit VARCHAR,
  last_meal_time TIMESTAMP,
  glucose_date_time TIMESTAMP,
  is_before_diabetes_diagnosis BOOLEAN,
  phq4_score INT,
  phq4_risk_level VARCHAR,
  cvd_risk_level VARCHAR,
  cvd_risk_score INT,
  refer_assessment BOOLEAN,
  is_latest BOOLEAN,
  is_before_htn_diagnosis BOOLEAN,
  bp_arm VARCHAR,
  bp_position VARCHAR,
  physically_active BOOLEAN,
  type VARCHAR,
  other_id_type VARCHAR,
  bplog_details jsonb,
  phq4_mental_health jsonb,
  id_type VARCHAR,
  preferred_name VARCHAR,
  is_family_diabetes_history BOOLEAN,
  is_before_gestational_diabetes BOOLEAN,
  screening_date_time TIMESTAMP WITH TIME ZONE,
  operating_unit_id BIGINT, FOREIGN KEY (operating_unit_id) REFERENCES operating_unit(id),
  account_id BIGINT, FOREIGN KEY (account_id) REFERENCES account(id),
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  county_id BIGINT, FOREIGN KEY (county_id) REFERENCES county(id),
  sub_county_id BIGINT, FOREIGN KEY (sub_county_id) REFERENCES sub_county(id),
  site_id BIGINT, FOREIGN KEY (site_id) REFERENCES site(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_tracker (
  id SERIAL PRIMARY KEY,
  program_id BIGINT,
  national_id VARCHAR,
  first_name VARCHAR,
  last_Name VARCHAR,
  age BIGINT,
  gender VARCHAR,
  phone_number VARCHAR,
  next_medical_review_date TIMESTAMP,
  patient_status VARCHAR,
  is_observation BOOLEAN,
  is_screening BOOLEAN,
  is_regular_smoker BOOLEAN,
  enrollment_at TIMESTAMP,
  cvd_risk_level VARCHAR,
  cvd_risk_score INT,
  last_review_date TIMESTAMP,
  is_confirm_diagnosis BOOLEAN,
  provisional_diagnosis VARCHAR[],
  confirm_diagnosis VARCHAR[],
  last_assessment_date TIMESTAMP,
  glucose_value FLOAT,
  glucose_type VARCHAR,
  glucose_unit VARCHAR,
  is_initial_review BOOLEAN,
  diagnosis_comments VARCHAR,
  height FLOAT,
  weight FLOAT,
  phq9_score INT,
  phq9_risk_level VARCHAR,
  gad7_score INT,
  gad7_risk_level VARCHAR,
  risk_level VARCHAR,
  is_red_risk_patient BOOLEAN,
  bmi FLOAT,
  phq4_second_score INT,
  phq4_first_score INT,
  phq4_score INT,
  avg_diastolic INT,
  screening_referral BOOLEAN,
  phq4_risk_level VARCHAR,
  avg_systolic INT,
  avg_pulse INT,
  date_of_birth TIMESTAMP,
  estimated_delivery_date TIMESTAMP,
  last_menstrual_period_date TIMESTAMP,
  is_pregnant BOOLEAN,
  is_labtest_referred BOOLEAN,
  is_medication_prescribed BOOLEAN,
  is_htn_diagnosis BOOLEAN,
  is_diabetes_diagnosis BOOLEAN,
  next_bg_assesment_date TIMESTAMP WITH TIME ZONE,
  next_bp_assesment_date TIMESTAMP WITH TIME ZONE,
  last_labtest_referred_date TIMESTAMP WITH TIME ZONE,
  last_medication_prescribed_date TIMESTAMP WITH TIME ZONE,
  screening_id BIGINT, FOREIGN KEY (screening_id) REFERENCES screening_log(id),
  patient_id BIGINT, FOREIGN KEY (patient_id) REFERENCES patient(id),
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  site_id BIGINT, FOREIGN KEY (site_id) REFERENCES site(id),
  operating_unit_id BIGINT, FOREIGN KEY (operating_unit_id) REFERENCES operating_unit(id),
  account_id BIGINT, FOREIGN KEY (account_id) REFERENCES account(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false,
  delete_reason VARCHAR,
  delete_other_reason VARCHAR
);

CREATE TABLE glucose_log (
  id SERIAL PRIMARY KEY,
  glucose_type VARCHAR,
  glucose_value FLOAT,
  glucose_unit VARCHAR,
  last_meal_time TIMESTAMP,
  glucose_date_time TIMESTAMP,
  hba1c FLOAT,
  is_latest BOOLEAN,
  type VARCHAR,
  is_updated_from_enrollment BOOLEAN,
  hba1c_unit VARCHAR,
  is_old_record boolean default false,
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  screening_id BIGINT, FOREIGN KEY (screening_id) REFERENCES screening_log(id),
  tenant_id BIGINT,
  assessment_tenant_id BIGINT,
  bg_taken_on TIMESTAMP WITH TIME ZONE,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE bp_log (
  id SERIAL PRIMARY KEY,
  avg_systolic INT,
  avg_diastolic INT,
  avg_pulse INT,
  height FLOAT,
  weight FLOAT,
  bmi FLOAT,
  temperature FLOAT,
  cvd_risk_level VARCHAR,
  cvd_risk_score INT,
  is_latest BOOLEAN,
  is_regular_smoker BOOLEAN,
  type VARCHAR,
  risk_level VARCHAR,
  bp_position VARCHAR,
  bp_arm VARCHAR,
  notes VARCHAR,
  is_updated_from_enrollment BOOLEAN,
  covid_vacc_status VARCHAR,
  assessment_landmark VARCHAR,
  assessment_category VARCHAR,
  insurance_id VARCHAR,
  insurance_type VARCHAR,
  insurance_status VARCHAR,
  other_insurance VARCHAR,
  bplog_details jsonb,
  is_old_record boolean default false,
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  screening_id BIGINT, FOREIGN KEY (screening_id) REFERENCES screening_log(id),
  tenant_id BIGINT,
  assessment_tenant_id BIGINT,
  bp_taken_on TIMESTAMP WITH TIME ZONE,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_assessment (
  id SERIAL PRIMARY KEY,
  bp_log_id BIGINT, FOREIGN KEY (bp_log_id) REFERENCES bp_log(id),
  glucose_log_id BIGINT, FOREIGN KEY (glucose_log_id) REFERENCES glucose_log(id),
  tenant_id BIGINT,
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  type VARCHAR,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE symptom (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  description VARCHAR,
  type VARCHAR,
  display_order INT,
  categories jsonb,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_symptom (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  other_symptom VARCHAR,
  type VARCHAR,
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  symptom_id BIGINT, FOREIGN KEY (symptom_id) REFERENCES symptom(id),
  patient_assessment_id BIGINT,  FOREIGN KEY (patient_assessment_id) REFERENCES patient_assessment(id),
  bp_log_id BIGINT, FOREIGN KEY (bp_log_id) REFERENCES bp_log(id),
  glucose_log_id BIGINT, FOREIGN KEY (glucose_log_id) REFERENCES glucose_log(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_visit (
  id SERIAL PRIMARY KEY,
  visit_date TIMESTAMP,
  is_prescription BOOLEAN,
  is_investigation BOOLEAN,
  is_medical_review BOOLEAN,
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE complication (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  status BOOLEAN,
  display_order INT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_complication (
  id SERIAL PRIMARY KEY,
  other_complication VARCHAR,
  complication_id BIGINT, FOREIGN KEY (complication_id) REFERENCES complication(id),
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  patient_visit_id BIGINT, FOREIGN KEY (patient_visit_id) REFERENCES patient_visit(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE diagnosis (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  description VARCHAR,
  display_order INT,
  type VARCHAR,
  gender VARCHAR,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_diagnosis (
  id SERIAL PRIMARY KEY,
  htn_year_of_diagnosis INT,
  diabetes_patient_type VARCHAR,
  htn_patient_type VARCHAR,
  is_htn_diagnosis BOOLEAN,
  diabetes_diagnosis VARCHAR,
  diabetes_year_of_diagnosis INT,
  is_diabetes_diagnosis BOOLEAN,
  diabetes_diag_controlled_type VARCHAR,
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  patient_visit_id BIGINT, FOREIGN KEY (patient_visit_id) REFERENCES patient_visit(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE comorbidity (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  status BOOLEAN,
  display_order INT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_comorbidity (
  id SERIAL PRIMARY KEY,
  other_comorbidity VARCHAR,
  comorbidity_id BIGINT, FOREIGN KEY (comorbidity_id) REFERENCES comorbidity(id),
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  patient_visit_id BIGINT, FOREIGN KEY (patient_visit_id) REFERENCES patient_visit(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE current_medication (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  type VARCHAR,
  status BOOLEAN,
  display_order INT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_current_medication (
  id SERIAL PRIMARY KEY,
  adhering_med_comment VARCHAR,
  is_adhering_current_med BOOLEAN,
  allergies_comment VARCHAR,
  is_drug_allergies BOOLEAN,
  current_medication_id BIGINT, FOREIGN KEY (current_medication_id) REFERENCES current_medication(id),
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  patient_visit_id BIGINT, FOREIGN KEY (patient_visit_id) REFERENCES patient_visit(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE lifestyle (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  type VARCHAR,
  answers jsonb,
  is_answer_dependent BOOLEAN,
  display_order INT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_lifestyle (
  id SERIAL PRIMARY KEY,
  answer VARCHAR,
  comments VARCHAR,
  lifestyle_id BIGINT, FOREIGN KEY (lifestyle_id) REFERENCES lifestyle(id),
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  patient_visit_id BIGINT, FOREIGN KEY (patient_visit_id) REFERENCES patient_visit(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE medical_compliance (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  status BOOLEAN,
  display_order INT,
  is_child_exists BOOLEAN,
  parent_compliance_id BIGINT, FOREIGN KEY (parent_compliance_id) REFERENCES medical_compliance(id),
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE physical_examination (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  description VARCHAR,
  display_order INT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE complaints (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  display_order INT,
  parent_compliance_id BIGINT, FOREIGN KEY (parent_compliance_id) REFERENCES medical_compliance(id),
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_medical_review (
  id SERIAL PRIMARY KEY,
  physical_exam_comments VARCHAR,
  complaint_comments VARCHAR,
  clinical_note VARCHAR,
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  patient_visit_id BIGINT, FOREIGN KEY (patient_visit_id) REFERENCES patient_visit(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_medical_review_physical_examination (
  patient_medical_review_id BIGINT, FOREIGN KEY (patient_medical_review_id) REFERENCES patient_medical_review(id),
  physical_examination_id BIGINT, FOREIGN KEY (physical_examination_id) REFERENCES physical_examination(id)
);

CREATE TABLE patient_medical_review_complaints (
  patient_medical_review_id BIGINT, FOREIGN KEY (patient_medical_review_id) REFERENCES patient_medical_review(id),
  complaint_id BIGINT, FOREIGN KEY (complaint_id) REFERENCES complaints(id)
);

CREATE TABLE red_risk_notification (
  id SERIAL PRIMARY KEY,
  status VARCHAR,
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  patient_assessment_id BIGINT, FOREIGN KEY (patient_assessment_id) REFERENCES patient_assessment(id),
  bp_log_id BIGINT, FOREIGN KEY (bp_log_id) REFERENCES bp_log(id),
  glucose_log_id BIGINT, FOREIGN KEY (glucose_log_id) REFERENCES glucose_log(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_medical_compliance (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  compliance_id BIGINT,
  other_compliance VARCHAR,
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  patient_assessment_id BIGINT, FOREIGN KEY (patient_assessment_id) REFERENCES patient_assessment(id),
  bp_log_id BIGINT, FOREIGN KEY (bp_log_id) REFERENCES bp_log(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE brand (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  display_order INT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_deleted BOOLEAN DEFAULT false,
  is_active BOOLEAN DEFAULT true
);

CREATE TABLE classification (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  display_order INT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_deleted BOOLEAN DEFAULT false,
  is_active BOOLEAN DEFAULT true
); 

CREATE TABLE dosage_form (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  display_order INT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_deleted BOOLEAN DEFAULT false,
  is_active BOOLEAN DEFAULT true
);

CREATE EXTENSION IF NOT EXISTS citext;

CREATE TABLE medication_country_detail (
  id SERIAL PRIMARY KEY,
  type VARCHAR,
  display_order INT,
  classification_name VARCHAR,
  medication_name citext,
  classification_id BIGINT, FOREIGN KEY (classification_id) REFERENCES classification(id),
  brand_name VARCHAR,
  brand_id BIGINT, FOREIGN KEY (brand_id) REFERENCES brand(id),
  dosage_unit_name VARCHAR,
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  dosage_form_name VARCHAR,
  dosage_form_id BIGINT, FOREIGN KEY (dosage_form_id) REFERENCES dosage_form(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false,
  CONSTRAINT  medication_unique_key UNIQUE (medication_name, country_id, classification_id, brand_id, dosage_form_id)
);

CREATE TABLE frequency (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  type VARCHAR,
  duration INT,
  period VARCHAR,
  risk_level VARCHAR,
  display_order INT,
  title VARCHAR,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE frequency_type (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  culture_id BIGINT,
  culture_value VARCHAR,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_treatment_plan (
  id SERIAL PRIMARY KEY,
  medical_review_frequency VARCHAR,
  bp_check_frequency VARCHAR,
  bg_check_frequency VARCHAR,
  hba1c_check_frequency VARCHAR,
  is_provisional BOOLEAN,
  risk_level VARCHAR,
  is_latest BOOLEAN,
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  patient_visit_id BIGINT, FOREIGN KEY (patient_visit_id) REFERENCES patient_visit(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_lab_test (
  id SERIAL PRIMARY KEY,
  lab_test_name VARCHAR,
  result_date TIMESTAMP,
  referred_by BIGINT,
  is_reviewed BOOLEAN,
  is_other_labtest BOOLEAN,
  result_update_by BIGINT,
  is_abnormal BOOLEAN,
  comment VARCHAR,
  lab_test_id BIGINT, FOREIGN KEY (lab_test_id) REFERENCES lab_test(id),
  patient_visit_id BIGINT, FOREIGN KEY (patient_visit_id) REFERENCES patient_visit(id),
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_lab_test_result(
  id SERIAL PRIMARY KEY,
  result_name VARCHAR,
  display_name VARCHAR,
  result_value VARCHAR,
  result_status VARCHAR,
  unit VARCHAR,
  is_abnormal BOOLEAN,
  lab_test_result_id BIGINT references lab_test_result(id),
  patient_lab_test_id BIGINT references patient_lab_test(id),
  lab_test_id BIGINT references lab_test(id),
  patient_track_id BIGINT references patient_tracker(id),
  patient_visit_id BIGINT references patient_visit(id),
  tenant_id BIGINT,
  display_order INT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE mental_health (
  id SERIAL PRIMARY KEY,
  phq4_second_score INT,
  phq4_first_score INT,
  phq4_score INT,
  phq4_risk_level VARCHAR,
  phq9_score INT,
  phq9_risk_level VARCHAR,
  gad7_score INT,
  gad7_risk_level VARCHAR,
  phq4_mental_health jsonb,
  phq9_mental_health jsonb,
  gad7_mental_health jsonb,
  is_latest BOOLEAN,
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  tenant_id BIGINT,
  assessment_tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);



CREATE TABLE account_customization (
  id SERIAL PRIMARY KEY,
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  account_id BIGINT, FOREIGN KEY (account_id) REFERENCES account(id),
  workflow VARCHAR,
  type VARCHAR,
  category VARCHAR,
  form_input VARCHAR,
  clinical_workflow_id BIGINT, FOREIGN KEY (clinical_workflow_id) REFERENCES clinical_workflow(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_pregnancy_details (
  id SERIAL PRIMARY KEY,
  diagnosis VARCHAR[],
  temperature NUMERIC(6, 2),
  estimated_delivery_date timestamp,
  parity INT,
  last_menstrual_period_date timestamp,
  diagnosis_time timestamp,
  gravida INT,
  pregnancy_fetuses_number INT,
  is_on_treatment BOOLEAN,
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE nutrition_lifestyle (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  display_order int,
  created_by BIGINT NOT NULL,
  updated_by BIGINT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_nutrition_lifestyle (
  id SERIAL PRIMARY KEY,
  lifestyle_assessment VARCHAR,
  referred_by BIGINT, FOREIGN KEY (referred_by) REFERENCES "user"(id),
  referred_date timestamp,
  assessed_by BIGINT, FOREIGN KEY (referred_by) REFERENCES "user"(id),
  assessed_date timestamp,
  clinician_note VARCHAR,
  other_note VARCHAR,
  is_viewed BOOLEAN,
  patient_visit_id BIGINT, FOREIGN KEY (patient_visit_id) REFERENCES patient_visit(id),
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE patient_nutrition_lifestyle_lifestyle (
  patient_nutrition_lifestyle_id BIGINT, FOREIGN KEY (patient_nutrition_lifestyle_id) REFERENCES patient_nutrition_lifestyle(id),
  nutrition_lifestyle_id BIGINT, FOREIGN KEY (nutrition_lifestyle_id) REFERENCES nutrition_lifestyle(id)
);

CREATE TABLE outbound_email(
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    tenant_id bigint,
    is_processed BOOLEAN,
    "to" VARCHAR,
    retry_attempts int,
    form_data_id bigint,
    form_name VARCHAR,
    type VARCHAR,
    body VARCHAR,
    subject VARCHAR,
    cc VARCHAR,
    bcc VARCHAR,
    is_active BOOLEAN DEFAULT true,
    is_deleted BOOLEAN DEFAULT false
);


create table prescription(
	id SERIAL PRIMARY KEY,
	tenant_id bigint,
	patient_visit_id bigint,
	patient_track_id bigint,FOREIGN KEY(patient_track_id) REFERENCES patient_tracker(id),
	medication_name VARCHAR,
	medication_id bigint,FOREIGN KEY(medication_id) REFERENCES medication_country_detail(id),
	classification_name VARCHAR,
	brand_name VARCHAR,
	dosage_form_name VARCHAR,
	dosage_unit_name VARCHAR,
	dosage_unit_value VARCHAR,
	dosage_unit_id bigint,
	dosage_frequency_name VARCHAR,
	dosage_frequency_id bigint,
	prescribed_days int,
	discontinued_on TIMESTAMP WITH TIME ZONE,
	discontinued_reason VARCHAR,
	end_date TIMESTAMP WITH TIME ZONE,
	instruction_note VARCHAR,
	remaining_prescription_days int,
	prescription_filled_days bigint NOT NULL,
	signature VARCHAR,
	reason VARCHAR,
	is_active  BOOLEAN DEFAULT true,
	is_deleted BOOLEAN DEFAULT false,
  created_by bigint NOT NULL,
	updated_by bigint,
	created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

create table prescription_history(
	id SERIAL PRIMARY KEY,
	tenant_id bigint,
	patient_visit_id bigint,
	patient_track_id bigint,FOREIGN KEY(patient_track_id) REFERENCES patient_tracker(id),
	prescription_id bigint,FOREIGN KEY(prescription_id) REFERENCES prescription(id),
	medication_name VARCHAR,
	medication_id bigint,FOREIGN KEY(medication_id) REFERENCES medication_country_detail(id),
	classification_name VARCHAR,
	brand_name VARCHAR,
	dosage_form_name VARCHAR,
	dosage_unit_name VARCHAR,
	dosage_unit_value VARCHAR,
	dosage_unit_id bigint,
	dosage_frequency_name VARCHAR,
	dosage_frequency_id bigint,
	prescribed_days int,
	end_date TIMESTAMP WITH TIME ZONE,
	instruction_note VARCHAR,
	remaining_prescription_days int,
	prescription_filled_days bigint,
	last_refill_date TIMESTAMP WITH TIME ZONE,
	signature VARCHAR,
	reason VARCHAR,
	is_active  BOOLEAN DEFAULT true,
	is_deleted BOOLEAN DEFAULT false,
  created_by bigint NOT NULL,
	updated_by bigint,
	created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customized_module(
  id SERIAL PRIMARY KEY,
  patient_track_id bigint references patient_tracker(id),
  clinical_workflow_id bigint references clinical_workflow(id),
  module_value jsonb,
  screen_type VARCHAR,
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

  CREATE TABLE audit(
    id SERIAL PRIMARY KEY,
    created_by VARCHAR NOT NULL,
    updated_by VARCHAR,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    entity VARCHAR,
    action VARCHAR,
    entity_id bigint,
    column_name VARCHAR,
    old_value VARCHAR,
    new_value VARCHAR
);

  CREATE TABLE api_role_permission (
    id SERIAL PRIMARY KEY,
    method VARCHAR,
    api VARCHAR,
    roles VARCHAR
);
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE risk_algorithm(
  id SERIAL PRIMARY KEY,
  risk_algorithm jsonb,
  country_id BIGINT, FOREIGN KEY (country_id) REFERENCES country(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  display_order int,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE reason(
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  type VARCHAR,
  display_order INT,
  created_by BIGINT,
  updated_by BIGINT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE dosage_frequency(
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  description VARCHAR,
  display_order INT,
  created_by BIGINT,
  updated_by BIGINT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE side_menu(
  id SERIAL PRIMARY KEY,
  role_name VARCHAR,
  menus jsonb,
  display_order INT,
  created_by BIGINT,
  updated_by BIGINT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

create table model_questions (
    id SERIAL PRIMARY KEY, 
    questions VARCHAR,
    display_order int,
    is_default BOOLEAN,
    is_mandatory BOOLEAN,
    "type" VARCHAR,
    country_id BIGINT, FOREIGN KEY(country_id) REFERENCES country(id),
    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true
);

CREATE TABLE model_answers (
    id SERIAL PRIMARY KEY,
    answer VARCHAR, 
    display_order int,
    is_default BOOLEAN,
    is_mandadory BOOLEAN,
    question_id BIGINT, FOREIGN KEY(question_id) REFERENCES model_questions(id),
    value int,
    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    culture_value VARCHAR,
  culture_id BIGINT, FOREIGN KEY (culture_id) REFERENCES culture(id)
);

CREATE TABLE country_customization (
    id SERIAL PRIMARY KEY, 
    form_input VARCHAR,
    type VARCHAR, 
    category VARCHAR,
    is_default BOOLEAN DEFAULT false,
    tenant_id BIGINT,
    country_id BIGINT, FOREIGN KEY(country_id) REFERENCES country(id),
    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    culture_id BIGINT
);

CREATE Table device_details (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  type varchar,
  model VARCHAR,
  version VARCHAR,
  aes_key VARCHAR,
  rsa_private_key VARCHAR,
  auth_tag VARCHAR,
  device_id VARCHAR,
  rsa_public_key VARCHAR,
  last_logged_in TIMESTAMP,
  user_id BIGINT ,FOREIGN KEY (user_id) REFERENCES "user"(id),
  tenant_id BIGINT,
  created_by BIGINT NOT NULL ,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE outbound_sms(
	id SERIAL PRIMARY KEY, 
	username VARCHAR,
	form_data_id BIGINT,
	retry_attempts INT DEFAULT 0,
	phone_number VARCHAR,
	body VARCHAR,
	is_processed BOOLEAN DEFAULT false,
	notification_id BIGINT,
	tenant_id BIGINT,
	created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sms_template(
	id SERIAL PRIMARY KEY,
	body VARCHAR,
	type VARCHAR,
	created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP 
);

CREATE TABLE sms_template_values(
	id SERIAL PRIMARY KEY,
	key VARCHAR,
	sms_template_id int, FOREIGN KEY (sms_template_id) REFERENCES sms_template(id),
	created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
	 
CREATE TABLE email_template (
  id SERIAL PRIMARY KEY,
  type VARCHAR,
  vm_content VARCHAR,
  body VARCHAR,
  title VARCHAR,
  app_url VARCHAR,
  app_type varchar NULL,
  subject varchar
);

CREATE TABLE email_template_value (
  id SERIAL PRIMARY KEY,
  name VARCHAR,
  email_template_id BIGINT,
  FOREIGN KEY (email_template_id) REFERENCES email_template(id)
);


INSERT INTO email_template (id,type,vm_content,body,title,app_url) VALUES
     (3,'User_Creation','vmContent','<div style=word-wrap: break-word; font-family: calibri; font-size: 12pt >    <p><img src="https://mdt-shruti.s3.ap-south-1.amazonaws.com/logo/logo1.png" style="height:100%;width:10%;"></p><hr>    <p style="font-size: 11pt">Dear SPICE Application User,<br><br>    <p>Welcome! An account has been created for you in the SPICE software application.    <br><br>    <strong>Your account username is your email address {{email}}. To finish setting up your SPICE application account, please click on this <a href="{{app_url_email}}" target="_blank" data-saferedirecturl="https://www.google.com/url?q=https://emp-test.medtronic.com//%23!/resetPassword?id%3D598ae4e0-b3be-4a22-af1f-0c41907ee41a&amp;source=gmail&amp;ust=1576230079041000&amp;usg=AFQjCNFOFH1Ynv-hmaRN51oPYElvjxDRCA">LINK</a> to create your password.</strong>    <br><br>    Once you have set up your password you will be able to access the application.    <br><br>    If you have any additional questions, please contact the Medtronic LABS Support Team at support@medtroniclabs.org.    <br>      <br>    Sincerely,    <br>    The Medtronic LABS SPICE Platform Support Team<p><img src="https://mdt-shruti.s3.ap-south-1.amazonaws.com/logo/logo2.png" style="height:100%;width:40%;"></p></div>','Email_notification','app_url_email'),
     (4,'Forgot_Password','vmContent','<div style="word-wrap: break-word; font-family: calibri; font-size: 12pt" >    <p><img src="https://mdt-shruti.s3.ap-south-1.amazonaws.com/logo/logo1.png" style="height:100%;width:10%;"></p><hr>    <p style="font-size: 11pt">Dear SPICE Application User,<br><br>    <p>We have received a request to reset your SPICE account password. It was initiated after you selected “Forgot Password” in the SPICE software application.    <br><br>    <strong>Please click on this <a href="{{app_url_email}}" target="_blank">LINK</a> to reset your password. The link will expire in 60 minutes.If it is expires before the reset is completed, click on Forgot Password in the application to reset it again.</strong>    <br><br>    If you did not initiate an account password reset process or have a SPICE software application account associated with this email address, it is possible that someone else might be trying to access your account. If so, please notify Medtronic LABS Support Team at support@medtroniclabs.org.    <br><br>    If you have any additional questions, please contact the Medtronic LABS Support Team at support@medtroniclabs.org.    <br>      <br>    Sincerely,    <br>    The Medtronic LABS SPICE Platform Support Team<p><img src="https://mdt-shruti.s3.ap-south-1.amazonaws.com/logo/logo2.png" style="height:100%;width:40%;"></p></div>','Email_notification','app_url_email');

INSERT INTO email_template_value (id,"name",email_template_id) VALUES
     (1,'app_url_email',3),
     (2,'app_url_email',4),
     (3,'email',3);
 
INSERT INTO sms_template (body,"type",created_at,updated_at) VALUES
	 ('A Red Alert Notification was received for one of your patients. Log into the SPICE application to review patient details.
','RED_RISK','2022-12-16 12:56:09.161','2022-12-16 12:56:09.161'),
	 ('Hi {{name}}, thank you for getting screened by Spice health worker from {{country}} site. Your registration code is {{patient_id}}.

','ENROLL_PATIENT','2022-12-16 12:56:09.161','2022-12-16 12:56:09.161');


INSERT INTO sms_template_values ("key",sms_template_id,created_at,updated_at) VALUES
	 ('name',2,'2022-12-16 12:57:17.811','2022-12-16 12:57:17.811'),
	 ('country',2,'2022-12-16 12:57:17.815','2022-12-16 12:57:17.815'),
	 ('patient_id',2,'2022-12-16 12:57:17.816','2022-12-16 12:57:17.816');

CREATE TABLE classification_brand (
    id SERIAL PRIMARY KEY,
    country_id bigint, FOREIGN KEY(country_id) REFERENCES country(id),
    brand_id bigint, FOREIGN KEY(brand_id) REFERENCES brand(id),
    classification_id bigint, FOREIGN KEY(classification_id) REFERENCES classification(id),
    tenant_id bigint,
    is_active  BOOLEAN DEFAULT true,
    is_deleted BOOLEAN DEFAULT false,
    created_by bigint NOT NULL,
    updated_by bigint NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE country_classification (
    id SERIAL PRIMARY KEY,
    country_id bigint, FOREIGN KEY(country_id) REFERENCES country(id),
    classification_id bigint, FOREIGN KEY(classification_id) REFERENCES classification(id),
    tenant_id bigint,
    is_active  BOOLEAN DEFAULT true,
    is_deleted BOOLEAN DEFAULT false,
    created_by bigint NOT NULL,
    updated_by bigint NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE form_meta_ui (
    id SERIAL PRIMARY KEY,
    components jsonb,
    form_name VARCHAR,
    is_active  BOOLEAN DEFAULT true,
    is_deleted BOOLEAN DEFAULT false,
    created_by bigint NOT NULL,
    updated_by bigint NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE patient_transfer (
  id SERIAL PRIMARY KEY,
  transfer_to BIGINT, FOREIGN KEY (transfer_to) REFERENCES "user"(id),
  transfer_by BIGINT, FOREIGN KEY (transfer_by) REFERENCES "user"(id),
  transfer_site BIGINT, FOREIGN KEY (transfer_site) REFERENCES site(id),
  old_site BIGINT, FOREIGN KEY (old_site) REFERENCES site(id),
  patient_track_id BIGINT, FOREIGN KEY (patient_track_id) REFERENCES patient_tracker(id),
  transfer_reason VARCHAR,
  transfer_status VARCHAR,
  reject_reason VARCHAR,
  old_program_id BIGINT,
  tenant_id BIGINT,
  is_show BOOLEAN DEFAULT true,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE user_token (
  id SERIAL PRIMARY KEY,
  auth_token VARCHAR,
  user_id BIGINT, FOREIGN KEY (user_id) REFERENCES "user"(id),
  client VARCHAR,
  created_by BIGINT NOT NULL,
  updated_by BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
  is_active BOOLEAN DEFAULT true,
  is_deleted BOOLEAN DEFAULT false
);

CREATE TABLE public.culture_values (
	id SERIAL PRIMARY KEY,
	form_data_id int8 NULL,
	form_name varchar NULL,
	culture_id int8 NULL,
	culture_value varchar NULL,
	created_by int8 NOT NULL,
	updated_by int8 NOT NULL,
	created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
	is_active bool NOT NULL DEFAULT true,
	is_deleted bool NOT NULL DEFAULT false,
	json_culture_value jsonb NULL
);

CREATE FUNCTION update_virtual_id(in_tenant_id bigint)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
    DECLARE out_virtualId bigint;
    BEGIN
       update organization set "sequence"  = sequence+1 where id = in_tenant_id;
      select "sequence" into out_virtualId from organization where id =  in_tenant_id;
    RETURN out_virtualId;
    EXCEPTION WHEN OTHERS THEN
    RAISE NOTICE 'Error in updating virtual id for patient';
    RETURN -1;
    END;
$function$
;

CREATE TABLE shedlock(
   name VARCHAR,
   lock_until VARCHAR,
   locked_at  VARCHAR,
   locked_by  VARCHAR
);


CREATE INDEX idx_username ON "user" (username);
CREATE INDEX idx_phone_number ON "user" (phone_number);
CREATE INDEX bp_log_track_id ON bp_log (patient_track_id);
CREATE INDEX idx_device_id ON device_details (device_id);
CREATE INDEX glucose_log_track_id ON glucose_log (patient_track_id);
CREATE INDEX idx_validate ON medication_country_detail (medication_name,dosage_form_id,brand_id,classification_id,country_id);
CREATE INDEX idx_classification_brand ON classification_brand (country_id,brand_id,classification_id);
CREATE INDEX idx_national_id ON patient (national_id);
CREATE INDEX patient_phone_number ON patient (phone_number);
CREATE INDEX idx_virtual_id ON patient (virtual_id);
CREATE INDEX assessment_track_id ON patient_assessment (patient_track_id);
CREATE INDEX complication_track_id ON patient_complication (patient_track_id);
CREATE INDEX current_medication_track_id ON patient_current_medication (patient_track_id);
CREATE INDEX diagnosis_track_id ON patient_diagnosis (patient_track_id);
CREATE INDEX lab_test_track_id ON patient_lab_test (patient_track_id);
CREATE INDEX lab_test_result_track_id ON patient_lab_test_result (patient_track_id);
CREATE INDEX idx_patient_lab_test_id ON patient_lab_test_result (patient_lab_test_id);
CREATE INDEX lifestyle_track_id ON patient_lifestyle (patient_track_id);
CREATE INDEX medical_compliance_track_id ON patient_medical_compliance (patient_track_id);
CREATE INDEX medical_review_track_id ON patient_medical_review (patient_track_id);
CREATE INDEX nutrition_lifestyle_track_id ON patient_nutrition_lifestyle (patient_track_id);
CREATE INDEX pregnancy_track_id ON patient_pregnancy_details (patient_track_id);
CREATE INDEX symptoms_track_id ON patient_symptom (patient_track_id);
CREATE INDEX transfer_track_id ON patient_transfer (patient_track_id);
CREATE INDEX treatment_plan_track_id ON patient_treatment_plan (patient_track_id);
CREATE INDEX visit_track_id ON patient_visit (patient_track_id);
CREATE INDEX prescription_track_id ON prescription (patient_track_id);
CREATE INDEX idx_patient_visit_id ON prescription (patient_visit_id);
CREATE INDEX prescription_history_track_id ON prescription_history (patient_track_id);
CREATE INDEX prescription_history_visit_id ON prescription_history (patient_visit_id);
CREATE INDEX notification_track_id ON red_risk_notification (patient_track_id);
-- CREATE INDEX idx_patient_track_id ON patient_visit (patient_track_id);
CREATE INDEX screening_log_national_id ON screening_log (national_id);
CREATE INDEX screening_log_phone_number ON screening_log (phone_number);
CREATE INDEX tracker_phone_number ON patient_tracker(phone_number);
CREATE INDEX tracker_national_id ON patient_tracker(national_id);
CREATE INDEX tracker_program_id ON patient_tracker(program_id);
CREATE INDEX tracker_patient_id ON patient_tracker(patient_id);
CREATE INDEX tracker_screening_id ON patient_tracker(screening_id);
CREATE INDEX tracker_first_name ON patient_tracker(first_name);
CREATE INDEX tracker_last_name ON patient_tracker(last_name);
CREATE INDEX patient_first_name ON patient(first_name);
CREATE INDEX patient_last_name ON patient(last_name);
create index result_id on lab_test_result_range(lab_test_result_id);


