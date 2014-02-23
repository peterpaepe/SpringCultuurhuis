package be.vdab.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import be.vdab.entities.Reservatie;
import be.vdab.entities.Voorstelling;
import be.vdab.services.VoorstellingService;

@Controller
@RequestMapping("/reservatiemandje")
class ReservatiemandjeController {
	private final VoorstellingService voorstellingService;
	private final Reservatiemandje reservatiemandje;

	@Autowired
	public ReservatiemandjeController(VoorstellingService voorstellingService,
			Reservatiemandje reservatiemandje) {
		this.voorstellingService = voorstellingService;
		this.reservatiemandje = reservatiemandje;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView toonReservatiemandje() {
		ModelAndView modelAndView = new ModelAndView("reservatiemandje");
		if (reservatiemandje.getReservatiemandje() != null
				&& !reservatiemandje.getReservatiemandje().isEmpty()) {
			Map<Voorstelling, Integer> mandje = new HashMap<Voorstelling, Integer>();
			List<Long> voorstellingsNrs = new ArrayList<Long>();
			for (Map.Entry<Long, Integer> entry : reservatiemandje
					.getReservatiemandje().entrySet()) {
				voorstellingsNrs.add(entry.getKey());
			}
			List<Voorstelling> voorstellingen = (ArrayList<Voorstelling>) voorstellingService
					.findAll(voorstellingsNrs);
			int i = 0;
			for (Map.Entry<Long, Integer> entry : reservatiemandje
					.getReservatiemandje().entrySet()) {
				mandje.put(voorstellingen.get(i), entry.getValue());
				i++;
			}
			modelAndView.addObject("mandje", mandje);
			modelAndView
					.addObject("totaalPrijs", Reservatie.getTotalValue(mandje));
		} else {
			modelAndView.addObject("fouten", "Uw winkelmandje is leeg.");
		}
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST, params = { "voorstellingsNr",
			"verwijderen" })
	public String reserveerPlaatsen(@RequestParam List<String> voorstellingsNr) {
		;
		if (reservatiemandje.getReservatiemandje() != null
				&& !reservatiemandje.getReservatiemandje().isEmpty()) {
			Map<Long, Integer> mandje = reservatiemandje.getReservatiemandje();
			for (String voorstellingsNummer : voorstellingsNr) {
				mandje.remove(Long.parseLong(voorstellingsNummer));
			}
			if (mandje.isEmpty()) {
				reservatiemandje.setReservatiemandje(null);
			} else {
				reservatiemandje.setReservatiemandje(mandje);
			}
		}
		return "redirect:/reservatiemandje";
	}

	@RequestMapping(method = RequestMethod.POST, params = "verwijderen")
	public String submitZonderVoorstellingNrs() {
		return "redirect:/reservatiemandje";
	}
}