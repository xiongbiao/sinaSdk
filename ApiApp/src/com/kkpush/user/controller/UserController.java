package com.kkpush.user.controller;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kkpush.account.domain.Developer;
import com.kkpush.model.Mailinfo;
import com.kkpush.user.service.UserService;
import com.kkpush.util.CommonConstants;
import com.kkpush.util.MD5;
import com.kkpush.util.SendMail;
import com.kkpush.util.SystemConfig;
import com.kkpush.util.TokenUtil;
import com.kkpush.util.VerificationCode;
import com.kkpush.util.WebInterface;
import com.kkpush.web.controller.PublicController;

@Controller
@RequestMapping("/developer")
public class UserController extends PublicController{ }
