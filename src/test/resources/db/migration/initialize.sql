DROP TABLE IF EXISTS `DB_UNIT_SAMPLE`;
CREATE TABLE `DB_UNIT_SAMPLE` (`id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
`first_name` varchar(255) NOT NULL DEFAULT '',
`last_name` varchar(255) NOT NULL DEFAULT '',
`created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`created_by` varchar(255) NOT NULL DEFAULT '',
`updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`updated_by` varchar(255) NOT NULL DEFAULT '',
PRIMARY KEY (`id`));