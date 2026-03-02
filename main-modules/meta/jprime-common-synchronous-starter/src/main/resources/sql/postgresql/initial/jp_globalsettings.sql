create schema if not exists jprime_globalsettings;

create table if not exists jprime_globalsettings.jp_globalsettings
(
  code     varchar not null,
  property jsonb   not null,
  value    jsonb   null,
  primary key (code)
);

comment on table jprime_globalsettings.jp_globalsettings is 'Настройки системы';

comment on column jprime_globalsettings.jp_globalsettings.code is 'Идентификатор настройки';
comment on column jprime_globalsettings.jp_globalsettings.property is 'Описание настройки в формате jpProperty';
comment on column jprime_globalsettings.jp_globalsettings.value is 'Значение настройки';
