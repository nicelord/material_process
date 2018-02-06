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
  user_login_id             bigint,
  tgl_invoice               datetime,
  customer_id               bigint,
  cc_person                 varchar(255),
  cc_dept                   varchar(255),
  tax                       integer,
  term                      varchar(255),
  tgl_angkut                datetime,
  nmr_kendaraan             varchar(255),
  sial                      varchar(255),
  nomor_po                  varchar(255),
  nomor_do                  varchar(255),
  nomor_spk_wo              varchar(255),
  keterangan                varchar(255),
  currency                  varchar(255),
  constraint uq_invoice_nomor_invoice unique (nomor_invoice),
  constraint pk_invoice primary key (id))
;

create table invoice2 (
  id                        bigint auto_increment not null,
  nomor_invoice             varchar(255) not null,
  user_login_id             bigint,
  tgl_invoice               datetime,
  customer_id               bigint,
  cc_person                 varchar(255),
  cc_dept                   varchar(255),
  tax                       integer,
  term                      varchar(255),
  tgl_angkut                datetime,
  nmr_kendaraan             varchar(255),
  sial                      varchar(255),
  nomor_po                  varchar(255),
  nomor_do                  varchar(255),
  nomor_spk_wo              varchar(255),
  keterangan                varchar(255),
  currency                  varchar(255),
  constraint uq_invoice2_nomor_invoice unique (nomor_invoice),
  constraint pk_invoice2 primary key (id))
;

create table invoice_item (
  id                        bigint auto_increment not null,
  penerimaan_id             bigint,
  invoice_id                bigint,
  jenis_item                varchar(255),
  jml_kemasan               bigint,
  satuan_kemasan            varchar(255),
  banyak                    bigint,
  harga_satuan              bigint,
  kemasan_ke                integer,
  transport_detail          varchar(255),
  constraint pk_invoice_item primary key (id))
;

create table invoice_item2 (
  id                        bigint auto_increment not null,
  invoice2_id               bigint,
  jml_kemasan               bigint,
  satuan_kemasan            varchar(255),
  banyak                    bigint,
  harga_satuan              bigint,
  kemasan_ke                integer,
  item_detail               varchar(255),
  constraint pk_invoice_item2 primary key (id))
;

create table jenis_limbah (
  id                        bigint auto_increment not null,
  kode_jenis                varchar(255) not null,
  keterangan                varchar(255),
  constraint uq_jenis_limbah_kode_jenis unique (kode_jenis),
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
  satuan_kemasan2           varchar(255),
  jml_kemasan2              bigint,
  satuan_kemasan3           varchar(255),
  jml_kemasan3              bigint,
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
  jenis_fisik               varchar(255),
  constraint uq_manifest_penerimaan_id unique (penerimaan_id),
  constraint uq_manifest_kode_manifest unique (kode_manifest),
  constraint pk_manifest primary key (id))
;

create table outbound_item (
  id                        bigint auto_increment not null,
  residu_id                 bigint,
  penerimaan_id             bigint,
  tgl_buat                  datetime,
  tgl_kirim                 datetime,
  user_login_id             bigint,
  nama_item                 varchar(255),
  satuan_kemasan            varchar(255),
  jml_kemasan               bigint,
  satuan_kemasan2           varchar(255),
  jml_kemasan2              bigint,
  satuan_kemasan3           varchar(255),
  jml_kemasan3              bigint,
  satuan_berat              varchar(255),
  jml_berat                 bigint,
  constraint uq_outbound_item_residu_id unique (residu_id),
  constraint uq_outbound_item_penerimaan_id unique (penerimaan_id),
  constraint pk_outbound_item primary key (id))
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
  sertifikat_id             bigint,
  in_reporting              tinyint(1) default 0,
  satuan_kemasan            varchar(255),
  jml_kemasan               bigint,
  satuan_kemasan2           varchar(255),
  jml_kemasan2              bigint,
  satuan_kemasan3           varchar(255),
  jml_kemasan3              bigint,
  satuan_berat              varchar(255),
  jml_berat                 bigint,
  user_penerima_id          bigint,
  tgl_penerimaan            datetime,
  lokasi_gudang             varchar(255),
  status_penerimaan         varchar(255),
  ket_revisi                varchar(255),
  is_diterima               tinyint(1) default 0,
  is_revisi                 tinyint(1) default 0,
  constraint pk_penerimaan primary key (id))
;

create table pengiriman (
  id                        bigint auto_increment not null,
  perusahaan_tujuan         varchar(255),
  nomor_pengiriman          varchar(255),
  tgl_kirim                 datetime,
  nomor_container           varchar(255),
  nomor_kolom               varchar(255),
  perusahaan_pengangkut     varchar(255),
  constraint pk_pengiriman primary key (id))
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
  tgl_proses                datetime,
  satuan_kemasan            varchar(255),
  jml_kemasan               bigint,
  satuan_kemasan2           varchar(255),
  jml_kemasan2              bigint,
  satuan_kemasan3           varchar(255),
  jml_kemasan3              bigint,
  satuan_berat              varchar(255),
  jml_berat                 bigint,
  keterangan                varchar(255),
  nama_limbah               varchar(255),
  constraint pk_prosess_limbah primary key (id))
;

create table residu (
  id                        bigint auto_increment not null,
  residu_id                 varchar(255),
  user_login_id             bigint,
  tgl_buat                  datetime,
  tgl_kirim                 datetime,
  gudang_penghasil          varchar(255),
  nama_residu               varchar(255),
  satuan_kemasan            varchar(255),
  jml_kemasan               bigint,
  satuan_kemasan2           varchar(255),
  jml_kemasan2              bigint,
  satuan_kemasan3           varchar(255),
  jml_kemasan3              bigint,
  satuan_berat              varchar(255),
  jml_berat                 bigint,
  tipe                      varchar(255),
  constraint pk_residu primary key (id))
;

create table sertifikat (
  id                        bigint auto_increment not null,
  nomor_sertifikat          varchar(255) not null,
  user_login_id             bigint,
  customer_id               bigint,
  tgl_sertifkat             datetime,
  penanda_tangan            varchar(255),
  jabatan_penanda_tangan    varchar(255),
  description               varchar(255),
  constraint uq_sertifikat_nomor_sertifikat unique (nomor_sertifikat),
  constraint pk_sertifikat primary key (id))
;

create table setting (
  id                        bigint auto_increment not null,
  nama_setting              varchar(255),
  nilai_setting             varchar(255),
  constraint pk_setting primary key (id))
;

create table store (
  id                        bigint auto_increment not null,
  pengiriman_id             bigint,
  outbound_item_id          bigint,
  kode_store                varchar(255),
  satuan_kemasan            varchar(255),
  jml_kemasan               bigint,
  satuan_berat              varchar(255),
  jml_berat                 bigint,
  kemasan_ke                integer,
  in_reporting              tinyint(1) default 0,
  constraint pk_store primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  nama                      varchar(255),
  username                  varchar(255) not null,
  password                  varchar(255),
  akses                     varchar(255),
  constraint uq_user_username unique (username),
  constraint pk_user primary key (id))
;


create table invoice_penerimaan (
  invoice_id                     bigint not null,
  penerimaan_id                  bigint not null,
  constraint pk_invoice_penerimaan primary key (invoice_id, penerimaan_id))
;
alter table invoice add constraint fk_invoice_userLogin_1 foreign key (user_login_id) references user (id) on delete restrict on update restrict;
create index ix_invoice_userLogin_1 on invoice (user_login_id);
alter table invoice add constraint fk_invoice_customer_2 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_invoice_customer_2 on invoice (customer_id);
alter table invoice2 add constraint fk_invoice2_userLogin_3 foreign key (user_login_id) references user (id) on delete restrict on update restrict;
create index ix_invoice2_userLogin_3 on invoice2 (user_login_id);
alter table invoice2 add constraint fk_invoice2_customer_4 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_invoice2_customer_4 on invoice2 (customer_id);
alter table invoice_item add constraint fk_invoice_item_penerimaan_5 foreign key (penerimaan_id) references penerimaan (id) on delete restrict on update restrict;
create index ix_invoice_item_penerimaan_5 on invoice_item (penerimaan_id);
alter table invoice_item add constraint fk_invoice_item_invoice_6 foreign key (invoice_id) references invoice (id) on delete restrict on update restrict;
create index ix_invoice_item_invoice_6 on invoice_item (invoice_id);
alter table invoice_item2 add constraint fk_invoice_item2_invoice2_7 foreign key (invoice2_id) references invoice2 (id) on delete restrict on update restrict;
create index ix_invoice_item2_invoice2_7 on invoice_item2 (invoice2_id);
alter table manifest add constraint fk_manifest_penerimaan_8 foreign key (penerimaan_id) references penerimaan (id) on delete restrict on update restrict;
create index ix_manifest_penerimaan_8 on manifest (penerimaan_id);
alter table manifest add constraint fk_manifest_customerPenghasil_9 foreign key (customer_penghasil_id) references customer (id) on delete restrict on update restrict;
create index ix_manifest_customerPenghasil_9 on manifest (customer_penghasil_id);
alter table manifest add constraint fk_manifest_jenisLimbah_10 foreign key (jenis_limbah_id) references jenis_limbah (id) on delete restrict on update restrict;
create index ix_manifest_jenisLimbah_10 on manifest (jenis_limbah_id);
alter table manifest add constraint fk_manifest_customerTujuan_11 foreign key (customer_tujuan_id) references customer (id) on delete restrict on update restrict;
create index ix_manifest_customerTujuan_11 on manifest (customer_tujuan_id);
alter table manifest add constraint fk_manifest_user_12 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_manifest_user_12 on manifest (user_id);
alter table manifest add constraint fk_manifest_userAkunting_13 foreign key (user_akunting_id) references user (id) on delete restrict on update restrict;
create index ix_manifest_userAkunting_13 on manifest (user_akunting_id);
alter table outbound_item add constraint fk_outbound_item_residu_14 foreign key (residu_id) references residu (id) on delete restrict on update restrict;
create index ix_outbound_item_residu_14 on outbound_item (residu_id);
alter table outbound_item add constraint fk_outbound_item_penerimaan_15 foreign key (penerimaan_id) references penerimaan (id) on delete restrict on update restrict;
create index ix_outbound_item_penerimaan_15 on outbound_item (penerimaan_id);
alter table outbound_item add constraint fk_outbound_item_userLogin_16 foreign key (user_login_id) references user (id) on delete restrict on update restrict;
create index ix_outbound_item_userLogin_16 on outbound_item (user_login_id);
alter table penerimaan add constraint fk_penerimaan_sertifikat_17 foreign key (sertifikat_id) references sertifikat (id) on delete restrict on update restrict;
create index ix_penerimaan_sertifikat_17 on penerimaan (sertifikat_id);
alter table penerimaan add constraint fk_penerimaan_userPenerima_18 foreign key (user_penerima_id) references user (id) on delete restrict on update restrict;
create index ix_penerimaan_userPenerima_18 on penerimaan (user_penerima_id);
alter table prosess_limbah add constraint fk_prosess_limbah_penerimaan_19 foreign key (penerimaan_id) references penerimaan (id) on delete restrict on update restrict;
create index ix_prosess_limbah_penerimaan_19 on prosess_limbah (penerimaan_id);
alter table prosess_limbah add constraint fk_prosess_limbah_userPengirim_20 foreign key (user_pengirim_id) references user (id) on delete restrict on update restrict;
create index ix_prosess_limbah_userPengirim_20 on prosess_limbah (user_pengirim_id);
alter table prosess_limbah add constraint fk_prosess_limbah_userPenerima_21 foreign key (user_penerima_id) references user (id) on delete restrict on update restrict;
create index ix_prosess_limbah_userPenerima_21 on prosess_limbah (user_penerima_id);
alter table residu add constraint fk_residu_userLogin_22 foreign key (user_login_id) references user (id) on delete restrict on update restrict;
create index ix_residu_userLogin_22 on residu (user_login_id);
alter table sertifikat add constraint fk_sertifikat_userLogin_23 foreign key (user_login_id) references user (id) on delete restrict on update restrict;
create index ix_sertifikat_userLogin_23 on sertifikat (user_login_id);
alter table sertifikat add constraint fk_sertifikat_customer_24 foreign key (customer_id) references customer (id) on delete restrict on update restrict;
create index ix_sertifikat_customer_24 on sertifikat (customer_id);
alter table store add constraint fk_store_pengiriman_25 foreign key (pengiriman_id) references pengiriman (id) on delete restrict on update restrict;
create index ix_store_pengiriman_25 on store (pengiriman_id);
alter table store add constraint fk_store_outboundItem_26 foreign key (outbound_item_id) references outbound_item (id) on delete restrict on update restrict;
create index ix_store_outboundItem_26 on store (outbound_item_id);



alter table invoice_penerimaan add constraint fk_invoice_penerimaan_invoice_01 foreign key (invoice_id) references invoice (id) on delete restrict on update restrict;

alter table invoice_penerimaan add constraint fk_invoice_penerimaan_penerimaan_02 foreign key (penerimaan_id) references penerimaan (id) on delete restrict on update restrict;
