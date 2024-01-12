const { Router } = require('express');

const router = Router();

router.get('/actuator/health', async (_req, res) => {
    try {
      return res.status(200).json({
        uptime: process.uptime(),
        status: 'UP',
        date: new Date(),
      });
    } catch (error) {
      return res.status(503).send();
    } 
  });
  
  module.exports = router;
