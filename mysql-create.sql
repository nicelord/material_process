create table customer (
  id                        bigint auto_increment not null,
  nama                      varchar(255),
  alamat                    varchar(255),
  nomor_kontak              varchar(255),
  nama_kontak               varchar(255),
  email                     varchar(255),
  fax                       varchar(255),
  npwp                      varchar(255),
  constraint pk_customer primary key (id))
;

create table invoice (
  id                        bigint auto_increment not null,
  nomor_invoice             varchar(255) not null,
  user_login                varchar(255),
  tgl_invoice               datetime,
  customer_id               bigint,
  cc_person                 varchar(255),
  cc_dept                   varchar(255),
  tax                       integer,
  term                      varchar(255),
  gate_pass                 varchar(255),
  tgl_angkut                datetime,
  nmr_kendaraan             varchar(255),
  sial                      varchar(255),
  constraint uq_invoice_nomor_invoice unique (nomor_invoice),
  constraint pk_invoice primary key (id))
;

create table jenis_limbah (
  id                        bigint auto_increment not null,
  kode_jenis                varchar(255),
  keterangan                varchar(255),
  constraint pk_jenis_limbah primary key (id))
;

create table manifest (
  id                        bigint auto_increment not null,
  penerimaan_id             bigint,
  kode_manifest             varchar(255) not null,
  customer_penghasil_id     bigint,
  jenis_limbah_id           bigint,
  karakteristik_limbah      varchar(255),
  nama_teknik_limbah        varchar(255),
  satuan_kemasan            varchar(255),
  jml_kemasan               bigint,
  satuan_berat              varchar(255),
  jml_berat                 bigint,
  nomor_kendaraan           varchar(255),
  tgl_approve               datetime,
  status_approval           varchar(255),
  ket_approval_akunting     varchar(255),
  customer_tujuan_id        bigint,
  tgl_buat                  datetime,
  user_id                   bigint,
  user_akunting_id          bigint,
  tgl_angkut                datetime,
  penanda_tangan            varchar(255),
  jabatan_penanda_tangan    varchar(255),
  nama_driver               varchar(255),
  constraint uq_manifest_penerimaan_id unique (penerimaan_id),
  constraint uq_manifest_kode_manifest unique (kode_manifest),
  constraint pk_manifest primary key (id))
;

create table penanda_tangan (
  id                        bigint auto_increment not null,
  nama                      varchar(255),
  nik                       varchar(255),
  jabatan                   varchar(255),
  status                    varchar(255),
  constraint pk_penanda_tangan primary key (id))
;

create table penerimaan (
  id                        bigint auto_increment not null,
  invoice_id                bigint,
  satuan_kemasan            varchar(255),
  jml_kemasan               bigint,
  satuan_berat              varchar(255),
  jml_berat                 bigint,
  user_penerima_id          bigint,
  tgl_penerimaan            datetime,
  lokasi_gudang             varchar(255),
  status_penerimaan         varchar(255),
  ket_revisi                varchar(255),
  is_diterima               tinyint(1) default 0,
  is_revisi                 tinyint(1) default 0,
  harga_satuan_invoice      bigint,
  constraint pk_penerimaan primary key (id))
;

create table prosess_limbah (
  id                        bigint auto_increment not null,
  penerimaan_id             bigint,
  gudang_pengirim           varchar(255),
  gudang_tujuan             varchar(255),
  user_pengirim_id          bigint,
  user_penerima_id          bigint,
  tgl_kirim                 datetime,
  tgl_terima                datetime,
  tgl_selesai               datetime,
  satuan_kemasan            varchar(255),
  jml_kemasan               bigint,
  satuan_berat              varchar(255),
  jml_berat                 bigint,
  constraint pk_prosess_limbah primary key (id))
;

create table setting (
  id                        bigint auto_increment not null,
  nama_setting              varchar(255),
  nilai_setting             varchar(255),
  constraint pk_setting primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  nama                      varchar(255),
  username                  varchar(255),
  password                  varchar(255),
  akses                     varchar(255),
  constraint pk_user primary key (id))
;

alter table invoice add constraint fk_invoice_customer_1 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_invoice_customer_1 on invoice (customer_id);
alter table manifest add constraint fk_manifest_penerimaan_2 foreign key (penerimaan_id) references penerimaan (id) on delete restrict on update restrict;
create index ix_manifest_penerimaan_2 on manifest (penerimaan_id);
alter table manifest add constraint fk_manifest_customerPenghasil_3 foreign key (customer_penghasil_id) references customer (id) on delete restrict on update restrict;
create index ix_manifest_customerPenghasil_3 on manifest (customer_penghasil_id);
alter table manifest add constraint fk_manifest_jenisLimbah_4 foreign key (jenis_limbah_id) references jenis_limbah (id) on delete restrict on update restrict;
create index ix_manifest_jenisLimbah_4 on manifest (jenis_limbah_id);
alter table manifest add constraint fk_manifest_customerTujuan_5 foreign key (customer_tujuan_id) references customer (id) on delete restrict on update restrict;
create index ix_manifest_customerTujuan_5 on manifest (customer_tujuan_id);
alter table manifest add constraint fk_manifest_user_6 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_manifest_user_6 on manifest (user_id);
alter table manifest add constraint fk_manifest_userAkunting_7 foreign key (user_akunting_id) references user (id) on delete restrict on update restrict;
create index ix_manifest_userAkunting_7 on manifest (user_akunting_id);
alter table penerimaan add constraint fk_penerimaan_invoice_8 foreign key (invoice_id) references invoice (id) on delete restrict on update restrict;
create index ix_penerimaan_invoice_8 on penerimaan (invoice_id);
alter table penerimaan add constraint fk_penerimaan_userPenerima_9 foreign key (user_penerima_id) references user (id) on delete restrict on update restrict;
create index ix_penerimaan_userPenerima_9 on penerimaan (user_penerima_id);
alter table prosess_limbah add constraint fk_prosess_limbah_penerimaan_10 foreign key (penerimaan_id) references penerimaan (id) on delete restrict on update restrict;
create index ix_prosess_limbah_penerimaan_10 on prosess_limbah (penerimaan_id);
alter table prosess_limbah add constraint fk_prosess_limbah_userPengirim_11 foreign key (user_pengirim_id) references user (id) on delete restrict on update restrict;
create index ix_prosess_limbah_userPengirim_11 on prosess_limbah (user_pengirim_id);
alter table prosess_limbah add constraint fk_prosess_limbah_userPenerima_12 foreign key (user_penerima_id) references user (id) on delete restrict on update restrict;
create index ix_prosess_limbah_userPenerima_12 on prosess_limbah (user_penerima_id);


