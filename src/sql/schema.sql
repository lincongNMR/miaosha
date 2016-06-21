--database initialization script
CREATE database seckill;

use seckill;

create table seckill(
  'seckill_id' bigint NOT NULL AUTO_INCREMENT ,
  'name' varchar(120) NOT NULL ,
  'number' int NOT NULL ,
  'start_time' TIMESTAMP NOT NULL ,
  'end_time' TIMESTAMP NOT NULL,
  'create_time' TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  PRIMARY KEY (seckill_id),
  INDEX idx_start_time(start_time)
  INDEX idx_end_time(end_time)
  INDEX idx_create_time(create_time)
) ENGINE = InnoDB AUTO_INCREMENT = 1000 default charset = utf8 comment = 'seckill '

-- initial data
INSERT INTO seckill(name,number,start_time,end_time)
values
('1000RMB for iphone6s',100,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
('100RMB for xiaomi6s',100,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
('200RMB for huawei6s',100,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
('600RMB for lianxiang6s',100,'2015-11-01 00:00:00','2015-11-02 00:00:00');

CREATE TABLE success_killed(
  'seckill_id' bitint NOT NULL ,
  'user_phone' bitint NOT NULL ,
  'state' tinyint not NULL DEFAULT -1,
  'create_time' TIMESTAMP NOT NULL 
) ENGINE = InnoDB AUTO_INCREMENT = 1000 default charset = utf8 comment = 'seckill detail'