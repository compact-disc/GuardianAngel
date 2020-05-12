-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 30, 2020 at 10:31 PM
-- Server version: 10.4.6-MariaDB
-- PHP Version: 7.3.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `guardian_angel`
--
CREATE DATABASE IF NOT EXISTS `guardian_angel` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `guardian_angel`;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `name` text NOT NULL,
  `id` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Truncate table before insert `admin`
--

TRUNCATE TABLE `admin`;
--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`name`, `id`) VALUES
('g_a_at_iu', '1183539742794760193');

-- --------------------------------------------------------

--
-- Table structure for table `likes`
--

CREATE TABLE `likes` (
  `id` text NOT NULL,
  `name` text NOT NULL,
  `date` text NOT NULL,
  `content` text NOT NULL,
  `source` text NOT NULL,
  `numberoffavorites` text NOT NULL,
  `twitterid` text NOT NULL,
  `geolocation` text NOT NULL,
  `sentimenttype` text NOT NULL,
  `sentimentscore` text NOT NULL,
  `sentimentverypositive` text NOT NULL,
  `sentimentpositive` text NOT NULL,
  `sentimentneutral` text NOT NULL,
  `sentimentnegative` text NOT NULL,
  `sentimentverynegative` text NOT NULL,
  `interactinguser` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Truncate table before insert `likes`
--

TRUNCATE TABLE `likes`;
--
-- Dumping data for table `likes`
--

INSERT INTO `likes` (`id`, `name`, `date`, `content`, `source`, `numberoffavorites`, `twitterid`, `geolocation`, `sentimenttype`, `sentimentscore`, `sentimentverypositive`, `sentimentpositive`, `sentimentneutral`, `sentimentnegative`, `sentimentverynegative`, `interactinguser`) VALUES
('1237810874003398657', 'elonmusk', 'Wed Mar 11 14:41:24 EDT 2020', 'We should be excited about the future & striving to go beyond the horizon!', 'Twitter for iPhone', '134456', '1183539742794760193', 'null', 'Positive', '3.0', '13.0%', '33.0%', '18.0%', '26.0%', '9.0%', 'g_a_at_iu'),
('1237808277834084353', 'elonmusk', 'Wed Mar 11 14:31:05 EDT 2020', 'Moral condemnation used not for morality, but as a weapon for manipulation is messed up', 'Twitter for iPhone', '66404', '1183539742794760193', 'null', 'Negative', '1.0', '2.0%', '4.0%', '21.0%', '62.0%', '12.0%', 'g_a_at_iu'),
('1237239239915569153', 'elonmusk', 'Tue Mar 10 00:49:56 EDT 2020', 'Tesla should have a mega rave cave under the Berlin Gigafatory', 'Twitter for iPhone', '78285', '1183539742794760193', 'null', 'Negative', '1.0', '2.0%', '5.0%', '34.0%', '51.0%', '8.0%', 'g_a_at_iu'),
('1233825382270324738', 'BBBaumgartner', 'Sat Feb 29 13:44:29 EST 2020', 'I’m TOTALLY gonna have a respectful conversation and see where it goes with this woman. #Holly #AmyRyan #TheBest https://t.co/XlWAsCG4xT', 'Twitter for iPhone', '11491', '1183539742794760193', 'null', 'Neutral', '2.0', '4.0%', '18.0%', '43.0%', '30.0%', '5.0%', 'g_a_at_iu'),
('1233053863860670464', 'BBBaumgartner', 'Thu Feb 27 10:38:44 EST 2020', 'Question: do you know what today is? Answer: it’s my favorite day of the year. That’s right folks... today is #NationalChiliDay and I’ve partnered with BUSH’S® Beans to share with you MY chili recipe to help you celebrate the biggest day of the year. #ad @BushsBeans https://t.co/0pSF7HSj3y', 'Twitter for iPhone', '140133', '1183539742794760193', 'null', 'Neutral', '2.0', '4.0%', '20.0%', '48.0%', '25.0%', '4.0%', 'g_a_at_iu'),
('1232667712515559425', 'sundarpichai', 'Wed Feb 26 09:04:19 EST 2020', 'Today I’m pleased to announce that Google will invest more than $10 billion in offices and data centers across the U.S in 2020, creating more opportunities in communities from Massachusetts to Texas. \nhttps://t.co/zyW4a8l52Z', 'Twitter for Android', '4809', '1183539742794760193', 'null', 'Neutral', '2.0', '4.0%', '15.0%', '63.0%', '14.0%', '3.0%', 'g_a_at_iu'),
('1220070433535463427', 'johnkrasinski', 'Wed Jan 22 14:47:13 EST 2020', 'Lovely to spend some time with this gentleman today. https://t.co/KRFELj0jGZ', 'Twitter for iPhone', '314250', '1183539742794760193', 'null', 'Neutral', '2.0', '4.0%', '15.0%', '63.0%', '14.0%', '3.0%', 'g_a_at_iu'),
('1102306856297549826', 'VancityReynolds', 'Sun Mar 03 15:36:49 EST 2019', 'It’s the 25th anniversary of John Candy’s passing. We cooked up a small tribute to a comedic genius and Canadian hero. If you haven’t seen much of his work, take a look at his films. He was a treasure. Thanks to @chriscandy4u and @therealjencandy. 🇨🇦 https://t.co/dHvuviKnBs', 'Twitter for iPhone', '322136', '1183539742794760193', 'null', 'Neutral', '2.0', '3.0%', '18.0%', '58.0%', '18.0%', '3.0%', 'g_a_at_iu');

-- --------------------------------------------------------

--
-- Table structure for table `mentions`
--

CREATE TABLE `mentions` (
  `id` text NOT NULL,
  `name` text NOT NULL,
  `date` text NOT NULL,
  `content` text NOT NULL,
  `isfavorited` text NOT NULL,
  `isretweeted` text NOT NULL,
  `isretweetedbyuser` text NOT NULL,
  `source` text NOT NULL,
  `numberoffavorites` text NOT NULL,
  `twitterid` text NOT NULL,
  `geolocation` text NOT NULL,
  `sentimenttype` text NOT NULL,
  `sentimentscore` text NOT NULL,
  `sentimentverypositive` text NOT NULL,
  `sentimentpositive` text NOT NULL,
  `sentimentneutral` text NOT NULL,
  `sentimentnegative` text NOT NULL,
  `sentimentverynegative` text NOT NULL,
  `interactinguser` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Truncate table before insert `mentions`
--

TRUNCATE TABLE `mentions`;
-- --------------------------------------------------------

--
-- Table structure for table `retweets`
--

CREATE TABLE `retweets` (
  `id` text NOT NULL,
  `name` text NOT NULL,
  `date` text NOT NULL,
  `content` text NOT NULL,
  `source` text NOT NULL,
  `numberoffavorites` text NOT NULL,
  `twitterid` text NOT NULL,
  `geolocation` text NOT NULL,
  `sentimenttype` text NOT NULL,
  `sentimentscore` text NOT NULL,
  `sentimentverypositive` text NOT NULL,
  `sentimentpositive` text NOT NULL,
  `sentimentneutral` text NOT NULL,
  `sentimentnegative` text NOT NULL,
  `sentimentverynegative` text NOT NULL,
  `interactinguser` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Truncate table before insert `retweets`
--

TRUNCATE TABLE `retweets`;
--
-- Dumping data for table `retweets`
--

INSERT INTO `retweets` (`id`, `name`, `date`, `content`, `source`, `numberoffavorites`, `twitterid`, `geolocation`, `sentimenttype`, `sentimentscore`, `sentimentverypositive`, `sentimentpositive`, `sentimentneutral`, `sentimentnegative`, `sentimentverynegative`, `interactinguser`) VALUES
('1237957676098879488', 'g_a_at_iu', 'Thu Mar 12 00:24:44 EDT 2020', 'RT @BBBaumgartner: Question: do you know what today is? Answer: it’s my favorite day of the year. That’s right folks... today is #NationalC…', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Positive', '3.0', '8.0%', '75.0%', '14.0%', '2.0%', '1.0%', 'g_a_at_iu'),
('1237957401611055105', 'g_a_at_iu', 'Thu Mar 12 00:23:39 EDT 2020', 'RT @VancityReynolds: It’s the 25th anniversary of John Candy’s passing. We cooked up a small tribute to a comedic genius and Canadian hero.…', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Neutral', '2.0', '0.0%', '0.0%', '100.0%', '0.0%', '0.0%', 'g_a_at_iu');

-- --------------------------------------------------------

--
-- Table structure for table `timeline`
--

CREATE TABLE `timeline` (
  `id` text NOT NULL,
  `name` text NOT NULL,
  `date` text NOT NULL,
  `content` text NOT NULL,
  `isfavorited` text NOT NULL,
  `isretweeted` text NOT NULL,
  `isretweetedbyuser` text NOT NULL,
  `source` text NOT NULL,
  `numberoffavorites` text NOT NULL,
  `twitterid` text NOT NULL,
  `geolocation` text NOT NULL,
  `sentimenttype` text NOT NULL,
  `sentimentscore` text NOT NULL,
  `sentimentverypositive` text NOT NULL,
  `sentimentpositive` text NOT NULL,
  `sentimentneutral` text NOT NULL,
  `sentimentnegative` text NOT NULL,
  `sentimentverynegative` text NOT NULL,
  `interactinguser` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Truncate table before insert `timeline`
--

TRUNCATE TABLE `timeline`;
--
-- Dumping data for table `timeline`
--

INSERT INTO `timeline` (`id`, `name`, `date`, `content`, `isfavorited`, `isretweeted`, `isretweetedbyuser`, `source`, `numberoffavorites`, `twitterid`, `geolocation`, `sentimenttype`, `sentimentscore`, `sentimentverypositive`, `sentimentpositive`, `sentimentneutral`, `sentimentnegative`, `sentimentverynegative`, `interactinguser`) VALUES
('1255709728543514624', 'g_a_at_iu', 'Thu Apr 30 00:05:03 EDT 2020', 'This is yet another tweet.', 'false', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Neutral', '2.0', '7.0%', '28.0%', '43.0%', '18.0%', '5.0%', 'g_a_at_iu'),
('1237957676098879488', 'g_a_at_iu', 'Thu Mar 12 00:24:44 EDT 2020', 'RT @BBBaumgartner: Question: do you know what today is? Answer: it’s my favorite day of the year. That’s right folks... today is #NationalC…', 'true', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Positive', '3.0', '8.0%', '75.0%', '14.0%', '2.0%', '1.0%', 'g_a_at_iu'),
('1237957401611055105', 'g_a_at_iu', 'Thu Mar 12 00:23:39 EDT 2020', 'RT @VancityReynolds: It’s the 25th anniversary of John Candy’s passing. We cooked up a small tribute to a comedic genius and Canadian hero.…', 'true', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Neutral', '2.0', '0.0%', '0.0%', '100.0%', '0.0%', '0.0%', 'g_a_at_iu'),
('1235420641244983296', 'g_a_at_iu', 'Wed Mar 04 23:23:28 EST 2020', 'This is a tweet in March', 'false', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Neutral', '2.0', '2.0%', '12.0%', '50.0%', '31.0%', '5.0%', 'g_a_at_iu'),
('1232908444493123584', 'g_a_at_iu', 'Thu Feb 27 01:00:54 EST 2020', 'This year is going to be great!', 'false', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Very positive', '4.0', '53.0%', '46.0%', '1.0%', '0.0%', '0.0%', 'g_a_at_iu'),
('1232907885891465216', 'g_a_at_iu', 'Thu Feb 27 00:58:40 EST 2020', 'This is yet another tweet', 'false', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Neutral', '2.0', '3.0%', '27.0%', '54.0%', '14.0%', '2.0%', 'g_a_at_iu'),
('1232907757021548544', 'g_a_at_iu', 'Thu Feb 27 00:58:10 EST 2020', 'Hello', 'false', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Neutral', '2.0', '4.0%', '15.0%', '63.0%', '14.0%', '3.0%', 'g_a_at_iu'),
('1232907560300224512', 'g_a_at_iu', 'Thu Feb 27 00:57:23 EST 2020', 'Today was pretty good.', 'false', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Positive', '3.0', '17.0%', '50.0%', '20.0%', '10.0%', '4.0%', 'g_a_at_iu'),
('1232904830630465536', 'g_a_at_iu', 'Thu Feb 27 00:46:32 EST 2020', 'I had a really wonderful day! It was so great!', 'false', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Very positive', '4.0', '53.0%', '42.0%', '4.0%', '1.0%', '1.0%', 'g_a_at_iu'),
('1232904434448990211', 'g_a_at_iu', 'Thu Feb 27 00:44:58 EST 2020', 'People are dumb.', 'false', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Negative', '1.0', '1.0%', '2.0%', '20.0%', '61.0%', '16.0%', 'g_a_at_iu'),
('1232904392397008896', 'g_a_at_iu', 'Thu Feb 27 00:44:48 EST 2020', 'I need more tweets to test.', 'false', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Neutral', '2.0', '2.0%', '11.0%', '48.0%', '35.0%', '5.0%', 'g_a_at_iu'),
('1232904330321199105', 'g_a_at_iu', 'Thu Feb 27 00:44:33 EST 2020', 'This is really terrible.', 'false', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Very negative', '0.0', '5.0%', '2.0%', '4.0%', '43.0%', '46.0%', 'g_a_at_iu'),
('1195379014023692289', 'g_a_at_iu', 'Fri Nov 15 11:32:20 EST 2019', 'This is another tweet', 'false', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Neutral', '2.0', '4.0%', '19.0%', '46.0%', '27.0%', '5.0%', 'g_a_at_iu'),
('1183540505545728001', 'g_a_at_iu', 'Sun Oct 13 20:30:20 EDT 2019', 'This is a tweet', 'false', 'false', 'false', 'Twitter Web App', '0', '1183539742794760193', 'null', 'Neutral', '2.0', '2.0%', '19.0%', '64.0%', '12.0%', '2.0%', 'g_a_at_iu');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
