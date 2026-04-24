create table if not exists data.t_customers
(
    c_id               bigserial
        constraint t_customers_pk
            primary key,
    c_customer_name    varchar(1000),
    c_customer_contact varchar(3000),
    c_kpp_code         varchar(50),
    c_inn_code         varchar(50)
);

create table if not exists data.t_calculations
(
    c_id                   bigserial
        constraint t_calculations_pk
            primary key,
    c_lot_name             varchar(1000),
    c_project_name         varchar(3000),
    c_date_of_create       timestamp,
    c_project_location     varchar(1000),
    c_total_sum            numeric(12, 2),
    c_calculated_pos_count integer,
    c_total_pos_count      integer,
    c_total_pos_percent    double precision,
    c_resource_folder      varchar(300),
    c_customer_id          bigint
        constraint t_calculations_t_customers_c_id_fk
            references t_customers
);

create table if not exists data.t_doc_resource
(
    c_id           bigserial
        constraint t_doc_resource_pk
            primary key,
    c_doc_name     varchar(500),
    c_doc_type     varchar(500),
    c_doc_size     varchar(500),
    c_key          varchar(500),
    c_doc_index    varchar(50),
    c_content_type character varying[],
    c_calc_id      bigint
        constraint t_doc_resource_t_calculations_c_id_fk
            references t_calculations
);

create table if not exists data.t_doc_result
(
    c_id       bigserial
        constraint t_doc_result_pk
            primary key,
    c_doc_name varchar(500),
    c_doc_type varchar(500),
    c_doc_size varchar(500),
    c_key      varchar(500),
    c_calc_id  bigint
        constraint t_doc_result_t_calculations_c_id_fk
            references t_calculations
);

create table if not exists data.t_partition
(
    c_id           bigserial
        constraint t_partition_pk
            primary key,
    c_position     varchar(10),
    c_partition    varchar(300),
    c_sum          numeric(12, 2),
    c_calculated   integer,
    c_total        integer,
    c_percent      double precision,
    c_calc_id      bigint
        constraint t_partition_t_calculations_c_id_fk
            references t_calculations,
    c_content_type varchar
);

create table if not exists data.t_uncalculated
(
    c_id           bigserial
        constraint t_uncalculated_pk
            primary key,
    c_partition    varchar(300),
    c_name         varchar(300),
    c_standart     varchar(300),
    c_quantity     varchar(10),
    c_quality      varchar(10),
    c_comment      varchar(1000),
    c_calc_id      bigint
        constraint t_uncalculated_t_calculations_c_id_fk
            references t_calculations,
    c_nomenclature varchar,
    c_position     varchar
);