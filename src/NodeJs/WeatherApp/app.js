const express = require("express");
require("dotenv").config();

const app = express();

app.use(express.static("public"));

async function getWeather(city) {
    try {
        const response = await fetch(
            `https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${process.env.WEATHER_KEY}&units=metric`
        );

        if (!response.ok) {
            throw new Error("Unable to fetch weather data");
        }

        const data = await response.json();

        return {
            city: data.name,
            forecast: data.weather[0].description,
            temperature: data.main.temp,
            humidity: data.main.humidity
        };

    } catch (error) {
        console.log("Error:", error.message);
        throw error;
    }
}

app.get("/weather", async (req, res) => {

    try {
        const city = req.query.city;

        if (!city) {
            return res.status(400).json({
                error: "Please provide a city"
            });
        }

        const weather = await getWeather(city);

        res.json(weather);

    } catch (error) {

        res.status(500).json({
            error: "Unable to get weather information"
        });
    }
});

app.listen(3000, () => {
    console.log("Weather app running on port 3000");
});