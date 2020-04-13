package com.ecommerce.controller;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.bean.Auction;
import com.ecommerce.bean.BidRate;
import com.ecommerce.bean.Token;
import com.ecommerce.bean.User;
import com.ecommerce.repository.AuctionRepo;
import com.ecommerce.repository.BidRateRepo;
import com.ecommerce.repository.MyProfile;
import com.ecommerce.repository.PasswordResetTokenRepo;
import com.ecommerce.repository.UserRepo;
import com.ecommerce.service.EmailService;
import com.ecommerce.service.PasswordReset;


@Controller
public class ApplicationController {
	@Autowired
	private EmailService emailSender;
	@Autowired
	private PasswordReset passwordReset;
	@Autowired
	private UserRepo userDao;
	@Autowired
	private Token token;
	@Autowired
	private PasswordResetTokenRepo passReset;
	@Autowired
	private User user;
	@Autowired
	private MyProfile profile;
	@Autowired
	private Auction auction;
	@Autowired
	private AuctionRepo auctionRepo;
	@Autowired
	private BidRate bidrate;
	@Autowired
	private BidRateRepo bidRateRepo;
	@Autowired
	private JdbcTemplate data;

	@GetMapping("/ecommerce")
	public String getHome() {

		return "index";
	}

	@GetMapping("/getSession")
	public String getSession(HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		user = profile.getUserDetails(name);
		session.setAttribute("userName", user.getName());
		session.setAttribute("userId", user.getId());

		return "redirect:/ecommerce";
	}

	@PostMapping("/ecommerce/signup")
	public String saveUsers(User institution, Model model) {
		String page = null;
		try {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			institution.setPassword(passwordEncoder.encode(institution.getPassword()));
			userDao.save(institution);
			page = "redirect:/ecommerce";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			page =  "message";
			model.addAttribute("message", "Something went wrong or you are already registered. Please try again.");
		}
		return page;
	}

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/loginFailed")
	public String failedlogin() {
		return "loginFailure";
	}

	@GetMapping("/hello")
	public String helloPage() {
		return "hello";
	}

	@GetMapping("/denied")
	public String acessdenied() {
		return "forbidden";
	}

	@GetMapping("/ecommerce/resetPassword")
	public String resetPass() {
		return "mailForPassword";
	}

	@PostMapping("/ecommerce/mailForReset")
	public String sendMail(String mail) throws MessagingException {

		String email = mail;
		String msg = null;

		if (passwordReset.getUser(email)) {
			if (passwordReset.previousTokenExpred(email)) {
				emailSender.emailSend(passwordReset.sendNewToken(email));
				msg = token.getTokenString();

			} else {
				token = passwordReset.previousToken(email);
				emailSender.emailSend(token);
				msg = token.getTokenString();
			}
		} else {
			System.out.println("You are not registered in this system");
		}

		return "index";

	}

	@GetMapping("/ecommerce/resetPass/{token}/{userId}")
	public String test(@PathVariable("token") String tokenString, @PathVariable("userId") String userId, Model model,
			final RedirectAttributes redirectAttributes) {
		String page = null;
		int id = Integer.parseInt(userId);
		if (passwordReset.isTokenTrue(id, tokenString)) {
			model.addAttribute("userid", id);
			page = "passwordForReset";
		} else if (!passwordReset.isTokenTrue(id, tokenString)) {
			String message = null;
			model.addAttribute("message", message);
			page = "message";
		}
		return page;
	}

	@GetMapping("/ecommerce/savePass")
	public String savePassword(String userid, String password) {
		int id = Integer.parseInt(userid);
		user = userDao.findById(id);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(password));
		userDao.save(user);
		// passReset.deleteByUserId(id);
		return "redirect:/ecommerce";
	}

	@GetMapping("/ecommerce/myprofile")
	public String myProfile(int id, Model model, HttpSession session) {
		user = profile.getUserProfile(id);
		try {
			String base64Image = Base64.getEncoder().encodeToString(user.getLogo());
			user.setBase64image(base64Image);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("user", user);
		
		
		
		
		List<Auction> auctions = new ArrayList<>();
		auctions = auctionRepo.findByUserId(user.getId());
		for (Auction auctionss : auctions) {
			//auction = auctionss;
			user = userDao.findById(auctionss.getUser().getId());
			try {
				String base64imageUser = Base64.getEncoder().encodeToString(user.getLogo());
				user.setBase64image(base64imageUser);
			} catch (Exception e) { // TODO
				e.printStackTrace();
				System.out.println("problem is here");
			}

			auctionss.setUser(user);
			try {
				String base64ImageProduct = Base64.getEncoder().encodeToString(auctionss.getProductImages());
				auctionss.setBase64image(base64ImageProduct);
			} catch (Exception e) { // TODO
				System.out.println("Something happened here.");
			}
		
		}
		model.addAttribute("auctions", auctions);
	
		List<Auction> auctionb = new ArrayList<>();
		List<BidRate> bids = new ArrayList<>();
		bids = bidRateRepo.findByUserId(user.getId());
		for(BidRate bid : bids) {
			auction = auctionRepo.findByUser(bid.getAuction().getId());
			auctionb.add(auction);
		}
		for (Auction auctionsss : auctionb) {
			//auction = auctionss;
			user = userDao.findById(auctionsss.getUser().getId());
			try {
				String base64imageUser = Base64.getEncoder().encodeToString(user.getLogo());
				user.setBase64image(base64imageUser);
			} catch (Exception e) { // TODO
				e.printStackTrace();
				System.out.println("problem is here");
			}

			auctionsss.setUser(user);
			try {
				String base64ImageProduct = Base64.getEncoder().encodeToString(auctionsss.getProductImages());
				auctionsss.setBase64image(base64ImageProduct);
			} catch (Exception e) { // TODO
				System.out.println("Something happened here.");
			}
		
		}
		
		model.addAttribute("auctionbs",auctionb );
		
		
		return "UserProfile";
		
		
	}

	@PostMapping("/ecommerce/update")
	public String saveUpdate(@RequestParam("file") MultipartFile file, String id, String location, String emailAdress,
			String phoneNumber) throws IOException {
		byte[] image = file.getBytes();
		// String base64Image = Base64.getEncoder().encodeToString(image);
		User userA = new User();
		int userId = Integer.parseInt(id);
		userA.setId(userId);
		user = userDao.findById(userA.getId());
		userA.setEmailAdress(emailAdress);
		userA.setLocation(location);
		userA.setLogo(image);
		userA.setName(user.getName());
		userA.setPassword(user.getPassword());
		userA.setPhoneNumber(phoneNumber);
		userA.setRole(user.getRole());
		userDao.save(userA);

		return "redirect:/ecommerce";
	}

	@GetMapping("/ecommerce/Auctions")
	// @ResponseBody
	public String getAuctionPage(Model model, HttpSession session) {
		
		
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String name = auth.getName();
			user = profile.getUserDetails(name);
			session.setAttribute("userName", user.getName());
			session.setAttribute("userId", user.getId());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List<Auction> auctions = new ArrayList<Auction>();
		auctions = auctionRepo.findAll();
		List<Auction> auctionWithUser = new ArrayList<Auction>();
		for (Auction auctionss : auctions) {
			auction = auctionss;
			user = userDao.findById(auctionss.getUser().getId());
			try {
				String base64imageUser = Base64.getEncoder().encodeToString(user.getLogo());
				user.setBase64image(base64imageUser);
			} catch (Exception e) { // TODO
				e.printStackTrace();
				System.out.println("problem is here");
			}

			auction.setUser(user);
			try {
				String base64ImageProduct = Base64.getEncoder().encodeToString(auctionss.getProductImages());
				auction.setBase64image(base64ImageProduct);
			} catch (Exception e) { // TODO
				System.out.println("Something happened here.");
			}
			auctionWithUser.add(auction);
		}
		model.addAttribute("auctions", auctionWithUser);

		return "Auctions";
		// return auctionWithUser;
	}

	@PostMapping("/ecommerce/CreateAuction")
	public String createAuctions(String deadTime, String productDescription, 
			HttpSession session,@RequestParam("uid") String id ,@RequestParam("productImages") MultipartFile picture, String title)
			throws IOException {
		// product.setProductDescription(productDescription);
		Auction auctionNew = new Auction();
		LocalDateTime dtime = LocalDateTime.parse(deadTime);
		LocalDateTime stime = LocalDateTime.now();
		byte[] image;
		try {
			image = picture.getBytes();
			auctionNew.setProductImages(image);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		auctionNew.setStartTime(stime);
		auctionNew.setDeadTime(dtime);
		auctionNew.setTitle(title);
		auctionNew.setProductDescription(productDescription);
		int usersId = Integer.parseInt(id);
		user.setId(usersId);
		auctionNew.setUser(user);
		auctionRepo.save(auctionNew);
		return "redirect:/ecommerce/Auctions";
	}

	@GetMapping("/ecommerce/ProductDetails")
	// @ResponseBody
	public String productDescriptionPage(@RequestParam("id") int auctionid, Model model) {
		auction = auctionRepo.findById(auctionid);
		try {
			String base64ImageProduct = Base64.getEncoder().encodeToString(auction.getProductImages());
			auction.setBase64image(base64ImageProduct);
			user = userDao.findById(auction.getUser().getId());
			String base64ImageUser = Base64.getEncoder().encodeToString(user.getLogo());
			user.setBase64image(base64ImageUser);
			auction.setUser(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("auction", auction);

		List<BidRate> bids = new ArrayList<>();
		bids = bidRateRepo.findByAuctionIdOrderByBidRateDesc(auctionid);

		for (BidRate bid : bids) {
			user = userDao.findById(bid.getUser().getId());
			bid.setUser(user);
		}

		model.addAttribute("bids", bids);
		
		return "productdetails";

	}

	@PostMapping("/ecommerce/bidding")
	public String bidding(@RequestParam("userid") int userid, @RequestParam("auctionid") int auctionid,
			@RequestParam("bidrate") long bid, Model model) {
		bidrate.setBidRate(bid);
		auction.setId(auctionid);
		bidrate.setAuction(auction);
		user.setId(userid);
		bidrate.setUser(user);
		bidRateRepo.save(bidrate);

		try {
			auction = auctionRepo.findById(auctionid);
			String base64ImageProduct = Base64.getEncoder().encodeToString(auction.getProductImages());
			auction.setBase64image(base64ImageProduct);
			user = userDao.findById(auction.getUser().getId());
			String base64ImageUser = Base64.getEncoder().encodeToString(user.getLogo());
			user.setBase64image(base64ImageUser);
			auction.setUser(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("auction", auction);

		List<BidRate> bids = new ArrayList<>();
		bids = bidRateRepo.findByAuctionIdOrderByBidRateDesc(auctionid);

		for (BidRate bidd : bids) {
			user = userDao.findById(bidd.getUser().getId());
			bidd.setUser(user);
		}

		model.addAttribute("bids", bids);
		return "productdetails";

	}
	@GetMapping("/ecommerce/admin")
	public String adminPanel(Model model) {
		long totalAuctions = auctionRepo.count();
		long totalUsers = userDao.count();
		long totalBids = bidRateRepo.count();
		model.addAttribute("tusers", totalUsers);
		model.addAttribute("auctions", totalAuctions);
		model.addAttribute("bids", totalBids);
		
		try {
			List<User> users = new ArrayList<>();
			users = userDao.findAll();
			try {
				for(User user: users) {
					String base64imageUser = Base64.getEncoder().encodeToString(user.getLogo());
					user.setBase64image(base64imageUser);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addAttribute("users", users);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "admin";
	}
	@GetMapping("/ecommerce/deleteProfile")
	public String deleteUser(@RequestParam("id") int id) {
		String queryBid = "delete from bid_rate where user_id = '"+id+"'";
		String queryAuction = "delete from auction where user_id = '"+id+"'";
		data.update(queryBid);
		data.update(queryAuction);
		userDao.deleteById(id);
		return "redirect:/ecommerce/admin";
	}
	

}
