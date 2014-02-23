package be.vdab.web;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import be.vdab.entities.Klant;
import be.vdab.entities.Reservatie;
import be.vdab.entities.Voorstelling;
import be.vdab.services.KlantService;
import be.vdab.services.ReservatieService;
import be.vdab.services.VoorstellingService;

@Controller
@RequestMapping("/bevestig_reservatie")
class BevestigReservatieController {
	private final KlantService klantService;
	private final ReservatieService reservatieService;
	private final VoorstellingService voorstellingService;
	private final Reservatiemandje reservatiemandje;
	private final MislukteReservering mislukteReservering;
	private final GelukteReservering gelukteReservering;

	@Autowired
	public BevestigReservatieController(KlantService klantService,
			Reservatiemandje reservatiemandje,
			ReservatieService reservatieService,
			VoorstellingService voorstellingService,
			MislukteReservering mislukteReservering,
			GelukteReservering gelukteReservering) {
		this.klantService = klantService;
		this.reservatieService = reservatieService;
		this.voorstellingService = voorstellingService;
		this.reservatiemandje = reservatiemandje;
		this.mislukteReservering = mislukteReservering;
		this.gelukteReservering = gelukteReservering;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView bevestig_reservatie(Principal principal) {
		ModelAndView modelAndView = new ModelAndView("bevestig_reservatie");
		if (reservatiemandje.getReservatiemandje() != null
				&& !reservatiemandje.getReservatiemandje().isEmpty()) {
			try {
				modelAndView.addObject("gebruikerGegevens",
						klantService.findByGebruikersnaam(principal.getName()));
			} catch (NullPointerException ex) {
			}
		} else {
			modelAndView
					.addObject("fouten",
							"U kan geen reservaties bevestigen want uw winkelmandje is leeg.");
		}
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, params = "error")
	public ModelAndView errorInloggen(@RequestParam String error) {
		ModelAndView modelAndView = new ModelAndView("bevestig_reservatie");
		modelAndView.addObject("fouten", "Foutieve aanmeldgegevens");
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST, params = "nieuweGebruiker")
	public String nieuweGebruiker() {
		return "redirect:/nieuwe_klant";
	}

	@RequestMapping(method = RequestMethod.POST, params = "bevestig")
	public String bevestigReservatie(Principal principal) {
		if (reservatiemandje.getReservatiemandje() != null
				& !reservatiemandje.getReservatiemandje().isEmpty()) {
			Map<Long, Integer> mandje = reservatiemandje.getReservatiemandje();
			Map<Long, Integer> mislukteReserveringen = new HashMap<Long, Integer>();
			Map<Long, Integer> gelukteReserveringen = new HashMap<Long, Integer>();
			Klant klant = klantService
					.findByGebruikersnaam(principal.getName());
			for (Map.Entry<Long, Integer> entry : mandje.entrySet()) {
				Voorstelling voorstelling = voorstellingService.read(entry
						.getKey());
				Reservatie reservatie = new Reservatie(entry.getValue(),
						voorstelling, klant);
				if (reservatieService.create(reservatie)) {
					gelukteReserveringen.put(entry.getKey(), entry.getValue());
				} else {
					mislukteReserveringen.put(entry.getKey(), entry.getValue());
				}
			}
			reservatiemandje.setReservatiemandje(null);
			mislukteReservering.setReservatiemandje(mislukteReserveringen);
			gelukteReservering.setReservatiemandje(gelukteReserveringen);
		}
		return "redirect:/overzicht";
	}
}