# BotTester
A simple genetic breeding AI for recognizing hand-drawn numbers.

According to the metadata on this file, I last edited this file in December of 2019. I don't remember exactly when I first started working on this or when I finished working on it. Nevertheless, this project is now defunct.

This model is based on reading handwritten numbers based on a set format. This project may or may not have been designed to read the MNIST database; I don't remember anymore.

The basic structure of this AI is that it generates batches of bots and performs an evolution-like process. Each bot is created with a field of floats that it will multiply with each of the data points in the image array. These products are summed and the difference between this sum and the correct answer is calculated. The bots with the lowest total difference will then be iterated on by generating new bots based off of them and randomly tweaking each field a small amount.

This program was heavily inspired by the CGP Grey video on AI. This attempts to use the basic structure of the genetic breeding model as described by Grey.
