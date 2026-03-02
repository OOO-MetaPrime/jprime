delete from jprime_globalsettings.jp_globalsettings where code in (
  'files.limits.maxFileSize',
  'files.limits.maxArchiveFilesSize',
  'files.limits.maxUploadFileCount'
);

insert into jprime_globalsettings.jp_globalsettings(code, property, value)
values  ('files.limits.maxFileSize',
        '{"code": "files.limits.maxFileSize", "name": "Максимальный размер загружаемого файла (Мб)", "type": "integer", "min":5, "mandatory": false}',
        '{"intValue":10}'),
        ('files.limits.maxArchiveFilesSize',
        '{"code": "files.limits.maxArchiveFilesSize", "name": "Максимальный размер файлов в архиве (Мб)", "type": "integer", "min":5, "mandatory": false}',
        '{"intValue":10}'),
        ('files.limits.maxUploadFileCount',
        '{"code": "files.limits.maxUploadFileCount", "name": "Максимальное количество файлов за одну загрузку", "type": "integer", "min":0, "mandatory": false}',
        '{"intValue":null}')
on conflict (code)
do update set property = excluded.property, value = excluded.value;