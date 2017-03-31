
-- Add SQL ALTER commands here

ALTER TABLE `digibid`.`item` CHANGE COLUMN `description` `description` LONGTEXT NOT NULL ;

-- Add SQL INSERT/UPDATE commands here

INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('1', 'Greece');
INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('2', 'Armenia');
INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('3', 'Hungary');
INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('4', 'Russia');
INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('5', 'Denmark');
INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('6', 'Netherlands');
INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('7', 'Georgia');
INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('8', 'Turkey');
INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('9', 'Spain');
INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('10', 'Cyprus');
INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('11', 'Finland');
INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('13', 'Switzerland');
INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('14', 'Austria');
INSERT INTO `digibid`.`country` (`id`, `name`) VALUES ('15', 'Portugal');

INSERT INTO `digibid`.`item_category` (`id`, `name`) VALUES ('1', 'Art');
INSERT INTO `digibid`.`item_category` (`id`, `name`) VALUES ('2', 'Music');
INSERT INTO `digibid`.`item_category` (`id`, `name`) VALUES ('3', 'Books');
INSERT INTO `digibid`.`item_category` (`id`, `name`) VALUES ('4', 'Electronics');
INSERT INTO `digibid`.`item_category` (`id`, `name`) VALUES ('5', 'Sports & Outdoors');
INSERT INTO `digibid`.`item_category` (`id`, `name`) VALUES ('6', 'Motors');
INSERT INTO `digibid`.`item_category` (`id`, `name`) VALUES ('7', 'Clothing');
INSERT INTO `digibid`.`item_category` (`id`, `name`) VALUES ('8', 'Collectibles');
INSERT INTO `digibid`.`item_category` (`id`, `name`) VALUES ('9', 'Home & Garden');

INSERT INTO `digibid`.`location` (`id`, `latitude`, `longitude`, `name`, `country_id`) VALUES (1, NULL, NULL, 'Athens', '1');
INSERT INTO `digibid`.`location` (`id`, `latitude`, `longitude`, `name`, `country_id`) VALUES (2, NULL, NULL, 'Thessaloniki', '1');

UPDATE `digibid`.`location` SET `latitude`='37.9908997', `longitude`='23.7033199' WHERE `id`=1;
UPDATE `digibid`.`location` SET `latitude`='40.6212524', `longitude`='22.9110078' WHERE `id`=2;

INSERT INTO `digibid`.`user` (`username`, `active`, `seller_rating`, `bidder_rating`, `address`, `creation_date`, `email`, `first_name`, `last_name`, `password`, `phone`, `ssn`, `location_id`) VALUES ('admin', 1, 0, 0, 'Oulof Palem 2-4', '2016-08-29 08:54:07', 'admin@digibid.di.uoa.gr', 'Admin', 'Admin', '$2a$10$PvnoTvVeAIbp1oD0qP3EAux54SLWyPpdDxo07ZSDdVtLYxUZIwqq2', '1111111111', '1111111111', '2');
INSERT INTO `digibid`.`user` (`username`, `active`, `seller_rating`, `bidder_rating`, `address`, `creation_date`, `email`, `first_name`, `last_name`, `password`, `phone`, `ssn`, `location_id`) VALUES ('giorgos', 1, 0, 0, 'Oulof Palem 2-4', '2016-08-30 15:54:07', 'giorgos@digibid.di.uoa.gr', 'Giorgos', 'Pozidis', '$2a$10$0SYSJJZrd95mwnd4uyEDwOldNhacab6Yg8vXjZqz1MzUCk50B4yX.', '1111111111', '1111111111', '1');
INSERT INTO `digibid`.`user` (`username`, `active`, `seller_rating`, `bidder_rating`, `address`, `creation_date`, `email`, `first_name`, `last_name`, `password`, `phone`, `ssn`, `location_id`) VALUES ('arshak', 1, 0, 0, 'Oulof Palem 2-4', '2016-08-29 08:54:07', 'arshak@digibid.di.uoa.gr', 'Arshak', 'Mehrabyan', '$2a$10$QyJ6Yigya7FD244m3kAaku8cAGtciui8dKNtv68htiWzsdVIpkAT6', '1111111111', '1111111111', '2');

INSERT INTO `digibid`.`user_role` (`user_username`, `roles`) VALUES ('admin','ADMIN');
INSERT INTO `digibid`.`user_role` (`user_username`, `roles`) VALUES ('giorgos','SELLER');
INSERT INTO `digibid`.`user_role` (`user_username`, `roles`) VALUES ('giorgos','BIDDER');
INSERT INTO `digibid`.`user_role` (`user_username`, `roles`) VALUES ('arshak','SELLER');
INSERT INTO `digibid`.`user_role` (`user_username`, `roles`) VALUES ('arshak','BIDDER');

INSERT INTO `digibid`.`item` (`id`, `description`, `ends`, `first_bid`, `name`, `price`, `started`, `location_id`, `seller_username`) VALUES (1, 'Design Patterns: Elements of Reusable Object-Oriented Software is a software engineering book describing software design patterns. The book''s authors are Erich Gamma, Richard Helm, Ralph Johnson and John Vlissides with a foreword by Grady Booch. The book is divided into two parts, with the first two chapters exploring the capabilities and pitfalls of object-oriented programming, and the remaining chapters describing 23 classic software design patterns. The book includes examples in C++ and Smalltalk.', '2016-10-22 00:22:00', '10', 'Design Patterns : Elements of Reusable Object-Oriented Software Int''l Edition', '40', '2016-08-29 11:00:00', 1, 'arshak');
INSERT INTO `digibid`.`item_category_mapping` (`item_id`, `item_category_id`) VALUES (1, 1);
INSERT INTO `digibid`.`item_category_mapping` (`item_id`, `item_category_id`) VALUES (1, 2);

-- TODO MUST CHANGE THE LOAD_FILE URL
# INSERT INTO `digibid`.`item_image` (`id`, `description`, `image`, `item_id`)VALUES (1,'Design Patterns : Elements of Reusable Object-Oriented Software Intl Edition', LOAD_FILE('/tmp/dperoos.jpeg'),1);

# INSERT INTO `digibid`.`bid` (`id`, `amount`, `time`, `bidder_username`, `item_id`) VALUES ('1', '21', '2016-09-18 14:55:40', 'giorgos', '1');
# INSERT INTO `digibid`.`bid` (`id`, `amount`, `time`, `bidder_username`, `item_id`) VALUES ('2', '22', '2016-09-18 14:59:40', 'giorgos', '1');