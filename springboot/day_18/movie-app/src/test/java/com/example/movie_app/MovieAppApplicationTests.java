package com.example.movie_app;

import com.example.movie_app.entity.*;
import com.example.movie_app.model.enums.MovieType;
import com.example.movie_app.model.enums.UserRole;
import com.example.movie_app.repository.*;
import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
class MovieAppApplicationTests {
	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ActorRepository actorRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private DirectorRepository directorRepository;

	@Autowired
	private GenresRepository genresRepository;

	@Autowired
	private EpisodeRepository episodeRepository;

	@Autowired
	private FavoritesRepository favoritesRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Test
	void encode_user_password(){
		List<User> users = userRepository.findAll();
		for (User user : users) {
			user.setPassword(passwordEncoder.encode("123"));
			userRepository.save(user);
		}
	}


	@Test
	void save_users(){
		Faker faker = new Faker();
		for (int i = 0; i < 50; i++) {
			String name = faker.name().fullName();
			User user = User.builder()
					.name(name)
					.email(faker.internet().emailAddress())
					.avatar("https://placehold.co/200x200?text=" + name.substring(0, 1).toUpperCase())
					.password("123")
					.role(i == 0 || i == 1 ? UserRole.ADMIN : UserRole.USER)
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build();
			userRepository.save(user);
		}
	}

	@Test
	void save_countries(){
		Faker faker = new Faker();
		Slugify slugify = Slugify.builder().build();
		for (int i = 0; i < 10; i++) {
			String name = faker.country().name();
			Country country = Country.builder()
					.name(name)
					.slug(slugify.slugify(name))
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build();
			countryRepository.save(country);
		}
	}

	@Test
	void save_genres(){
		Faker faker = new Faker();
		Slugify slugify = Slugify.builder().build();
		for (int i = 0; i < 10; i++) {
			String name = faker.leagueOfLegends().champion();
			Genres genres = Genres.builder()
					.name(name)
					.slug(slugify.slugify(name))
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build();
			genresRepository.save(genres);
		}
	}

	@Test
	void save_actors(){
		Faker faker = new Faker();
		Slugify slugify = Slugify.builder().build();
		for (int i = 0; i < 50; i++) {
			String name = faker.name().fullName();
			Actor actor = Actor.builder()
					.name(name)
					.slug(slugify.slugify(name))
					.avatar("https://placehold.co/200x200?text=" + name.substring(0, 1).toUpperCase())
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build();
			actorRepository.save(actor);
		}
	}

	@Test
	void save_directors(){
		Faker faker = new Faker();
		Slugify slugify = Slugify.builder().build();
		for (int i = 0; i < 20; i++) {
			String name = faker.name().fullName();
			Director director = Director.builder()
					.name(name)
					.slug(slugify.slugify(name))
					.avatar("https://placehold.co/200x200?text=" + name.substring(0, 1).toUpperCase())
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.build();
			directorRepository.save(director);
		}
	}


	@Test
	void save_blogs() {
		Faker faker = new Faker();
		Random rd = new Random();
		Slugify slugify = Slugify.builder().build();

		List<User> users = userRepository.findByRole(UserRole.ADMIN);

		for (int i = 0; i < 100; i++) {
			// random 1 user
			try {
				User rdUser = users.get(rd.nextInt(users.size()));

				String title = faker.book().title();
				boolean status = rd.nextInt(2) == 0;
				Blog blog = Blog.builder()
						.title(title)
						.description(faker.lorem().paragraph())
						.slug(slugify.slugify(title))
						.content(faker.lorem().paragraph(100))
						.thumbnail(generateLinkImage(title))
						.status(status)
						.createdAt(LocalDateTime.now())
						.updatedAt(LocalDateTime.now())
						.publishedAt(status ? LocalDateTime.now() : null)
						.user(rdUser)
						.build();
				blogRepository.save(blog);
			} catch (Exception e) {
				System.err.println("Error saving blog: " + e.getMessage());
			}
		}
	}

	private String generateLinkImage(String name) {
		return "https://placehold.co/200x200?text=" + name.substring(0, 1).toUpperCase();
	}



	@Test
	void save_movies() {
		Faker faker = new Faker();
		Slugify slugify = Slugify.builder().build();
		Random rd = new Random();

		List<Country> countries = countryRepository.findAll();
		List<Actor> actors = actorRepository.findAll();
		List<Director> directors = directorRepository.findAll();
		List<Genres> genres = genresRepository.findAll();

		for (int i = 0; i < 150; i++) {
			// Random 1 country
			Country rdCountry = countries.get(rd.nextInt(countries.size()));

			// Random 1 -> 3 genres
			List<Genres> rdGenres = new ArrayList<>();
			for (int j = 0; j < rd.nextInt(3) + 1; j++) {
				Genres rdGenre = genres.get(rd.nextInt(genres.size()));
				if (!rdGenres.contains(rdGenre)) {
					rdGenres.add(rdGenre);
				}
			}

			// Random 5 -> 7 actors
			List<Actor> rdActors = new ArrayList<>();
			for (int j = 0; j < rd.nextInt(3) + 5; j++) {
				Actor rdActor = actors.get(rd.nextInt(actors.size()));
				if (!rdActors.contains(rdActor)) {
					rdActors.add(rdActor);
				}
			}

			// Random 1 -> 3 directors
			List<Director> rdDirectors = new ArrayList<>();
			for (int j = 0; j < rd.nextInt(3) + 1; j++) {
				Director rdDirector = directors.get(rd.nextInt(directors.size()));
				if (!rdDirectors.contains(rdDirector)) {
					rdDirectors.add(rdDirector);
				}
			}

			String name = faker.name().fullName();
			Boolean status = faker.bool().bool();
			Movie movie = Movie.builder()
					.name(name)
					.slug(slugify.slugify(name))
					.description(faker.lorem().paragraph())
					.poster("https://placehold.co/200x200?text=" + name.substring(0, 1).toUpperCase())
					.releaseYear(faker.number().numberBetween(2020, 2024))
					.rating(faker.number().randomDouble(1, 6, 10))
					.trailerUrl("https://www.youtube.com/embed/gCUg6Td5fgQ?si=OCtNkpFF03gq03ny")
					.type(MovieType.values()[rd.nextInt(MovieType.values().length)])
					.status(status)
					.createdAt(LocalDateTime.now())
					.updatedAt(LocalDateTime.now())
					.publishedAt(status ? LocalDateTime.now() : null)
					.country(rdCountry)
					.actors(rdActors)
					.directors(rdDirectors)
					.genres(rdGenres)
					.build();
			movieRepository.save(movie)
			;
		}
	}

	@Test
	void save_reviews() {
		Faker faker = new Faker();
		Random rd = new Random();

		List<Movie> movies = movieRepository.findAll();
		List<User> users = userRepository.findAll();

		for (Movie movie : movies) {
			// Create 10 -> 20 reviews for each movie
			for (int i = 0; i < rd.nextInt(11) + 10; i++) {
				// Random 1 user
				User rdUser = users.get(rd.nextInt(users.size()));

				Review review = Review.builder()
						.content(faker.lorem().paragraph())
						.rating(rd.nextInt(6) + 5)
						.movie(movie)
						.user(rdUser)
						.createdAt(LocalDateTime.now())
						.updatedAt(LocalDateTime.now())
						.build();
				reviewRepository.save(review);
			}
		}
	}


	@Test
	void save_episodes() {
		Faker faker = new Faker();
		Random rd = new Random();

		List<Movie> movies = movieRepository.findAll();

		for (Movie movie : movies) {
//			try {
				if (movie.getType().equals(MovieType.PHIM_BO)) {
					// Random 5 -> 10 episodes for each movie
					for (int i = 0; i < rd.nextInt(6) + 5; i++) {
						Episode episode = Episode.builder()
								.name("Tap " + (i + 1))
								.duration(rd.nextInt(31) + 30)
								.displayOrder(i + 1)
								.videoUrl("https://www.youtube.com/embed/gCUg6Td5fgQ?si=OCtNkpFF03gq03ny")
								.movie(movie)
								.status(true)
								.createdAt(LocalDateTime.now())
								.updatedAt(LocalDateTime.now())
								.publishedAt(LocalDateTime.now())
								.build();
						episodeRepository.save(episode);
					}
				} else {
					Episode episode = Episode.builder()
							.name("Tap full")
							.duration(rd.nextInt(91) + 30)
							.displayOrder(1)
							.videoUrl("https://www.youtube.com/embed/gCUg6Td5fgQ?si=OCtNkpFF03gq03ny")
							.movie(movie)
							.status(true)
							.createdAt(LocalDateTime.now())
							.updatedAt(LocalDateTime.now())
							.publishedAt(LocalDateTime.now())
							.build();
					episodeRepository.save(episode);
				}
//			} catch (Exception e) {
//				System.out.println("Error: " + e.getMessage());
//			}

		}
	}

	@Test
	void save_histories() {
		Faker faker = new Faker();
		Random rd = new Random();

		List<Movie> movies = movieRepository.findAll();
		List<User> users = userRepository.findAll();
		List<Episode> episodes = episodeRepository.findAll();

		for (User user : users) {
			// Random 10 -> 20 histories for each user
			for (int i = 0; i < rd.nextInt(11) + 10; i++) {
				// Random 1 movie
				Movie rdMovie = movies.get(rd.nextInt(movies.size()));
				// Random 1 episode
				Episode rdEpisode = episodes.get(rd.nextInt(episodes.size()));

				double duration = 0.1 + (2.0 - 0.1) * rd.nextDouble();

				History history = History.builder()
						.user(user)
						.movie(rdMovie)
						.episode(rdEpisode)
						.duration(duration)
						.createdAt(LocalDateTime.now())
						.updatedAt(LocalDateTime.now())
						.build();
				historyRepository.save(history);
			}
		}
	}

	@Test
	void save_favorites() {
		Faker faker = new Faker();
		Random rd = new Random();

		List<Movie> movies = movieRepository.findAll();
		List<User> users = userRepository.findAll();

		for (User user : users) {
			// Random 10 -> 20 favorites for each user
			for (int i = 0; i < rd.nextInt(11) + 10; i++) {
				// Random 1 movie
				Movie rdMovie = movies.get(rd.nextInt(movies.size()));

				Favorites favorite = Favorites.builder()
						.user(user)
						.movie(rdMovie)
						.createdAt(LocalDateTime.now())
						.build();
				favoritesRepository.save(favorite);
			}
		}
	}


	@Test
	void test_methods() {
		List<Movie> movies = movieRepository.findAll();
		System.out.println("Movies size: " + movies.size());

		Movie movie1 = movieRepository.findById(1).orElse(null);
		System.out.println("Movie 1: " + movie1);

		List<Movie> moviesByIds = movieRepository.findAllById(List.of(1, 2, 3));
		System.out.println("Movies by ids: " + moviesByIds);

		//Update
		movie1.setName("Chúa tể những chiếc nhẫn");
		movieRepository.save(movie1);

		//Delete
//		movieRepository.delete(movie1); //delete from movies where id = ?
//		movieRepository.deleteById(2);
//		movieRepository.deleteAllById(List.of(1, 2, 3));
//		movieRepository.deleteAll();

	}

	@Test
	public void testMethodQuery() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Movie> pageData = movieRepository.findByStatus(true, pageable);
		System.out.println("Total page: " + pageData.getTotalPages());
		System.out.println("Total element: " + pageData.getTotalElements());
		System.out.println("Is First: " + pageData.isFirst());

		pageData.getContent().forEach(System.out::println);

	}




}
